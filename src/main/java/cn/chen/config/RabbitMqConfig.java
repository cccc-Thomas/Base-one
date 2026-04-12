package cn.chen.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 核心配置类
 * 功能覆盖：
 * 1. Fanout 广播模式：消息发送到交换机后，所有绑定队列都会收到
 * 2. Topic 主题模式：通过通配符路由键，精准匹配队列
 * 3. 死信队列：消费失败的消息自动转发到死信队列，避免消息丢失
 */
@Configuration
public class RabbitMqConfig {

    // ======================== 1. Fanout 广播模式 常量定义 ========================
    // Fanout 交换机名称
    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    // 广播队列1（用于接收广播消息）
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    // 广播队列2（用于接收广播消息）
    public static final String FANOUT_QUEUE2 = "fanout.queue2";

    /**
     * 声明 Fanout 类型交换机
     * @return FanoutExchange 广播交换机
     * 特点：不处理路由键，消息发送到交换机后，会转发给所有绑定的队列
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        // durable: true 表示交换机持久化，服务重启后不丢失
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    /**
     * 声明广播队列1
     * @return Queue 持久化队列
     */
    @Bean
    public Queue fanoutQueue1() {
        return QueueBuilder.durable(FANOUT_QUEUE1).build();
    }

    /**
     * 声明广播队列2
     * @return Queue 持久化队列
     */
    @Bean
    public Queue fanoutQueue2() {
        return QueueBuilder.durable(FANOUT_QUEUE2).build();
    }

    /**
     * 绑定队列1到广播交换机
     * @return Binding 绑定关系
     */
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    /**
     * 绑定队列2到广播交换机
     * @return Binding 绑定关系
     */
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    // ======================== 2. Topic 主题模式 常量定义 ========================
    // Topic 交换机名称
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    // Topic 队列名称
    public static final String TOPIC_QUEUE = "topic.queue";
    // Topic 路由键（通配符：# 匹配0个或多个单词，* 匹配1个单词）
    // user.# 会匹配 user.save、user.update、user.info.detail 等所有以 user. 开头的路由键
    public static final String TOPIC_ROUTING_KEY = "user.#";

    /**
     * 声明 Topic 类型交换机
     * @return TopicExchange 主题交换机
     * 特点：通过路由键通配符，精准匹配队列，实现灵活的消息路由
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    /**
     * 声明 Topic 队列
     * @return Queue 持久化队列
     */
    @Bean
    public Queue topicQueue() {
        return QueueBuilder.durable(TOPIC_QUEUE).build();
    }

    /**
     * 绑定 Topic 队列到交换机，并指定路由键
     * @return Binding 绑定关系
     */
    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue())
                .to(topicExchange())
                .with(TOPIC_ROUTING_KEY);
    }

    // ======================== 3. 死信队列 常量定义 ========================
    // 业务队列（正常业务消息的队列）
    public static final String BUSINESS_QUEUE = "business.queue";
    // 死信交换机（用于转发消费失败的消息）
    public static final String DEAD_LETTER_EXCHANGE = "dead.exchange";
    // 死信队列（存储消费失败的消息）
    public static final String DEAD_LETTER_QUEUE = "dead.queue";
    // 死信路由键
    public static final String DEAD_ROUTING_KEY = "dead.key";

    /**
     * 声明业务队列（绑定死信交换机）
     * @return Queue 持久化队列
     * 核心：通过 deadLetterExchange 和 deadLetterRoutingKey 配置死信转发规则
     * 当消息消费失败（手动nack）、超时未确认时，自动转发到死信队列
     */
    @Bean
    public Queue businessQueue() {
        return QueueBuilder.durable(BUSINESS_QUEUE)
                // 绑定死信交换机
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                // 绑定死信路由键
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .build();
    }

    /**
     * 声明死信交换机（Direct 类型，精准路由）
     * @return DirectExchange 直连交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    /**
     * 声明死信队列
     * @return Queue 持久化队列
     */
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**
     * 绑定死信队列到死信交换机
     * @return Binding 绑定关系
     */
    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue())
                .to(deadExchange())
                .with(DEAD_ROUTING_KEY);
    }
}
