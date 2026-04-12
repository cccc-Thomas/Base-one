package cn.chen.controller;

import cn.chen.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * RabbitMQ 消息生产者
 * 功能：发送不同模式的消息，并实现消息可靠性投递回调
 */
@Slf4j  // 日志注解，用于打印日志
@RestController  // 声明为REST接口，方便HTTP调用测试
public class RabbitProducer {

    // 注入RabbitTemplate：Spring AMQP提供的核心操作类，用于发送消息
    private final RabbitTemplate rabbitTemplate;

    // 构造方法注入（推荐，比@Autowired更安全）
    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 初始化消息可靠性投递回调
     * @PostConstruct：Bean初始化完成后自动执行该方法
     */
    @PostConstruct
    public void initCallback() {
        // ======================== 1. 消息到达交换机的确认回调（ConfirmCallback） ========================
        // 作用：确认消息是否成功到达交换机
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                // ack=true：消息成功到达交换机
                log.info("【消息确认】消息已成功到达交换机");
            } else {
                // ack=false：消息未到达交换机，打印失败原因
                log.error("【消息确认】消息未到达交换机，失败原因：{}", cause);
            }
        });

        // ======================== 2. 消息无法路由到队列的回调（ReturnCallback） ========================
        // 作用：消息到达交换机，但无法路由到任何队列时触发
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("【消息路由失败】交换机：{}，路由键：{}，错误码：{}，原因：{}",
                    exchange, routingKey, replyCode, replyText);
        });
    }

    /**
     * 发送广播模式消息
     * @param msg 消息内容
     * @return 发送结果
     * 特点：发送到Fanout交换机，所有绑定队列都会收到消息，无需路由键
     */
    @GetMapping("/sendFanout")
    public String sendFanout(String msg) {
        // convertAndSend：发送消息
        // 参数1：交换机名称；参数2：路由键（Fanout模式无需，传空字符串）；参数3：消息内容
        rabbitTemplate.convertAndSend(RabbitMqConfig.FANOUT_EXCHANGE, "", msg);
        return "广播消息发送成功：" + msg;
    }

    /**
     * 发送Topic模式消息
     * @param msg 消息内容
     * @return 发送结果
     * 特点：通过路由键匹配，只有符合通配符规则的队列会收到消息
     */
    @GetMapping("/sendTopic")
    public String sendTopic(String msg) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.TOPIC_EXCHANGE,  // 交换机名称
                "user.save",                     // 路由键（匹配user.#通配符）
                msg                              // 消息内容
        );
        return "Topic消息发送成功：" + msg;
    }

    /**
     * 发送业务队列消息（测试死信、WebSocket推送）
     * @param msg 消息内容
     * @return 发送结果
     */
    @GetMapping("/sendBusiness")
    public String sendBusiness(String msg) {
        // CorrelationData：消息唯一标识，用于消息追踪和确认
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.BUSINESS_QUEUE,
                (Object) msg,
                new CorrelationData()
        );
        return "业务消息发送成功：" + msg;
    }
}