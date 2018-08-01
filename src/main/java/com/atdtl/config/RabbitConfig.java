package com.atdtl.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Rabbitmq 配置
 * 如果手动创建过或者RabbitMQ中已经存在该队列那么也可以省略下述代码…
 *
 * @author Administrator
 * @since 2018/7/23 15:28
 */
@Configuration
public class RabbitConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitConfig.class);

    public static final String DEFAULT_BOOK_QUEUE = "dev.book.register.default.queue";
    public static final String MANUAL_BOOK_QUEUE = "dev.book.register.manual.queue";

    @Bean
    public Queue defaultBookQueue(){
        // 第一个是 QUEUE 的名字，第二个是消息是否需要持久化处理
        return new Queue(DEFAULT_BOOK_QUEUE,true);
    }

    @Bean
    public Queue manualBookQueue(){
        // 第一个是 QUEUE 的名字，第二个是消息是否需要持久化处理
        return new Queue(MANUAL_BOOK_QUEUE,true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause));
        rabbitTemplate.setReturnCallback(
                (message, replayCode, replayText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message{}", exchange, routingKey, replayCode, replayText, message)
        );
        return rabbitTemplate;
    }

    /**
     *  延迟队列 TTL 名称
     */
    private static final String REGISTER_DELAY_QUEUE = "dev.book.register.delay.queue";

    /**
     *  DLX, dead letter发送到的 exchange
     *  TODO 此处的 exchange 很重要, 具体消息就是发到该交换机
     */
    public static final String REGISTER_DELAY_EXCHANGE = "dev.book.register.delay.exchange";

    /**
     *  routing key 名称
     *  TODO 此处的 routing 很重要, 具体消息发送在该 routing 的
     */
    public static final String DELAY_ROUTING_KEY = "";

    public static final String REGISTER_QUEUE_NAME = "dev.book.regist.queue";
    public static final String REGISTER_EXCHANGE_NAME = "dev.book.regist.exchange";
    public static final String ROUTING_KEY = "all";

    /**
     * 延迟队列配置
     * <p>
     *     TODO 第一种方式是直接设置 Queue 延迟时间   但如果直接给队列设置过期时间，这种做法不太灵活
     *     1.param.put("x-message-ttl", 5*100);
     *
     *     TODO 第一种方式就是每次发送消息动态设置延迟时间
     *     2.rabbitTeplate.convertAndSend(book, message -> {message.getMessageProperties().setExpiration(2*1000 + ""); return message;});
     *
     *     二者是兼容的,默认是时间最小的优先
     *
     * </p>
     * @return  Queue
     */
    @Bean
    public Queue delayProcessQueue(){
        Map<String, Object> params = new HashMap<>();
        // TODO: 2018/7/23  x-dead-letter-exchange声明队列里的死信转发到的DLX名称
        params.put("x-dead-letter-exchange",REGISTER_EXCHANGE_NAME);
        // TODO: 2018/7/23  x-dead-letter-routing-key声明了这些死信在转发时携带的 routing-key 名称
        params.put("x-dead-letter-routing-key",ROUTING_KEY);
        return new Queue(REGISTER_DELAY_QUEUE, true, false, false, params);
    }

    /**
     *
     *  需要将一个队列绑定到交换机上, 要求该消息与一个特定的路由键完全匹配
     *  这是一个完整匹配。
     *  TODO  它不像 TopicExchange 那样可以使用通配符适配多个
     * @return  DirectExchange
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(REGISTER_DELAY_EXCHANGE);
    }

    @Bean
    public Binding dlxBinging(){
        return BindingBuilder.bind(delayProcessQueue()).to(directExchange()).with(DELAY_ROUTING_KEY);
    }

    @Bean
    public Queue registerBookQueue() {
        return new Queue(REGISTER_QUEUE_NAME, true);
    }

    /**
     *  将路由键和某模式进行匹配。此时队列需要绑定在一个模式上3
     *  符号“#”一个或多个词，符号“*”匹配单个词。因此，"audit.#"能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”。
     * @return
     */
    @Bean
    public TopicExchange registerBookTopicExchange() {
        return new TopicExchange(REGISTER_EXCHANGE_NAME);
    }

    @Bean
    public Binding registerBookBinging() {
        // TODO: 2018/7/23  如果要让延迟队列之间有关联, 这里的 绑定交换机 和 routingKey 很关键
        return BindingBuilder.bind(registerBookQueue()).to(registerBookTopicExchange()).with(ROUTING_KEY);
    }
}
