package cn.chen.controller;

import cn.chen.config.RabbitMqConfig;
import cn.chen.websocket.WebSocketServer;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 消息消费者
 * 功能：监听所有队列，手动ACK，异常处理，死信转发，WebSocket实时推送
 */
@Slf4j
@Component  // 交给Spring容器管理，自动注册消费者
public class RabbitConsumer {

    // ======================== 1. Fanout 广播队列1 消费者 ========================
    /**
     * 监听fanout.queue1队列，接收广播消息
     * @param msg 消息内容
     * @param channel RabbitMQ信道，用于手动ACK
     * @param message 消息对象（包含消息属性、deliveryTag等）
     */
    @RabbitListener(queues = RabbitMqConfig.FANOUT_QUEUE1)
    public void fanoutQueue1(String msg, Channel channel, Message message) {
        try {
            log.info("【Fanout队列1】收到消息：{}", msg);
            // 手动ACK：确认消息消费成功，从队列中移除
            // 参数1：deliveryTag：消息唯一标识；      参数2：multiple：是否批量确认（false表示只确认当前消息）
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("【Fanout队列1】消费异常", e);
            // 消费异常：手动NACK，拒绝消息
            // 参数3：requeue：是否重新入队（false表示不重入，直接转发到死信队列）
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                log.error("【Fanout队列1】NACK异常", ex);
            }
        }
    }

    // ======================== 2. Fanout 广播队列2 消费者 ========================
    @RabbitListener(queues = RabbitMqConfig.FANOUT_QUEUE2)
    public void fanoutQueue2(String msg, Channel channel, Message message) {
        try {
            log.info("【Fanout队列2】收到消息：{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("【Fanout队列2】消费异常", e);
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                log.error("【Fanout队列2】NACK异常", ex);
            }
        }
    }

    // ======================== 3. Topic 主题队列 消费者 ========================
    /**
     * 监听topic.queue队列，接收Topic模式消息
     */
    @RabbitListener(queues = RabbitMqConfig.TOPIC_QUEUE)
    public void topicQueue(String msg, Channel channel, Message message) {
        try {
            log.info("【Topic队列】收到消息：{}", msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("【Topic队列】消费异常", e);
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                log.error("【Topic队列】NACK异常", ex);
            }
        }
    }

    // ======================== 4. 业务队列 消费者（核心：WebSocket推送+死信） ========================
    /**
     * 监听business.queue队列，处理业务消息
     * 核心功能：
     * 1. 手动ACK控制消息消费
     * 2. 异常时转发到死信队列
     * 3. 消费成功后通过WebSocket广播给前端
     */
    @RabbitListener(queues = RabbitMqConfig.BUSINESS_QUEUE)
    public void businessQueue(String msg, Channel channel, Message message) throws Exception {
        log.info("【业务队列】收到消息：{}", msg);

        try {
            // ====================== 模拟业务逻辑（可替换为实际业务） ======================
            // 模拟业务异常：取消注释可测试死信队列
            // int i = 1 / 0;

            // ====================== 手动ACK：确认消息消费成功 ======================
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            // ====================== 核心：通过WebSocket广播给所有在线前端 ======================
            // 消费成功后，将消息推送给所有连接的WebSocket客户端，实现实时通知
            WebSocketServer.broadcast("【MQ实时推送】" + msg);

        } catch (Exception e) {
            log.error("【业务队列】消费异常，消息将转发到死信队列", e);
            // ====================== 手动NACK：拒绝消息，转发到死信队列 ======================
            // requeue=false：消息不重新入队，直接按死信规则转发
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    // ======================== 5. 死信队列 消费者 ========================
    /**
     * 监听dead.queue队列，处理消费失败的消息
     * 作用：记录失败日志、人工重试、告警通知等
     */
    @RabbitListener(queues = RabbitMqConfig.DEAD_LETTER_QUEUE)
    public void deadQueue(String msg, Channel channel, Message message) {
        log.error("【死信队列】收到消费失败的消息：{}", msg);
        try {
            // 手动ACK：确认死信消息消费成功，从死信队列移除
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            // ====================== 死信处理逻辑（可扩展） ======================
            // 1. 记录失败日志到数据库
            // 2. 发送告警通知（邮件、短信）
            // 3. 人工重试队列
        } catch (Exception e) {
            log.error("【死信队列】消费异常", e);
        }
    }
}