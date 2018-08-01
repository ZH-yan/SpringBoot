package com.atdtl.handler;

import com.atdtl.config.RabbitConfig;
import com.atdtl.entity.Book;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @since 2018/7/23 16:00
 */
@Component
public class BookHandler {

    public static final Logger log = LoggerFactory.getLogger(BookHandler.class);

    /**
     * <p>
     *     TODO 该方案是Spring-boot-data-amqp 默认的方式，不太推荐。具体使用 listenerManualAck()
     *     默认情况下，如果没有配置手动ACK，那么 Spring Data AMQP 会在消息消费完毕后自动帮我们去ACK
     *     存在问题：如果报错，消息不会丢失，但是会无限循环消费，一直报错，如果开启了错误日志很容易就把磁盘耗完
     *     解决方案：手动ACK，或者try-catch，然后在catch里面将错误消息转移到其他的系列中去
     *     spring.rabbitmq.listener.simple.acknowledge-mode=manual
     * </p>
     *
     * @param book  监听的内容
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitConfig.DEFAULT_BOOK_QUEUE})
    public void listenerAutoAck(Book book, Message message, Channel channel){
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // TODO: 2018/7/23 如果手动ACK 消息会被监听消费，但是消息在队列中依旧存在，如果未配置acknowledge-mode 默认是会在消费完毕后自动ACK掉
            log.info("[listenerAutoAck 监听的消息] - [{}]", book.toString());
            // TODO: 2018/7/23 通知MQ 消息已被成功消费，可以ACK了
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                // TODO: 2018/7/23 处理失败 重新压入MQ
                channel.basicRecover();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = {RabbitConfig.MANUAL_BOOK_QUEUE})
    public void listenerManulAck(Book book, Message message, Channel channel){
        try {
            log.info("[listenerAutoAck 监听的消息] - [{}]", book.toString());
            // TODO: 2018/7/23  通知MQ 消息已被成功消费，可以ACK了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e){
            // TODO: 2018/7/23 如果报错，那么我们可以进行容错处理，比如转移当前消息到其他队列

        }
    }

    @RabbitListener(queues = {RabbitConfig.REGISTER_QUEUE_NAME})
    public void listenerDelayQueue(Book book, Message message, Channel channel){
        try {
            log.info("[listenerDelayQueue 监听的消息] - [消费时间] - [{}] - [{}]", LocalDateTime.now(), book.toString());
            // TODO: 2018/7/23  通知MQ 消息已被成功消费，可以ACK了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // TODO: 2018/7/23 如果报错，那么我们可以进行容错处理，比如转移当前消息到其他队列
            e.printStackTrace();
        }
    }
}
