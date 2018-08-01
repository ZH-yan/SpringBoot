package com.atdtl.controller;

import com.atdtl.annotation.CacheLock;
import com.atdtl.annotation.CacheParam;
import com.atdtl.annotation.LocalLock;
import com.atdtl.config.RabbitConfig;
import com.atdtl.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 用于消息发送工作
 *
 * @author Administrator
 * @since 2018/7/23 15:43
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public BookController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public void defaultMessage(){
        Book book = new Book();
        book.setId("1");
        book.setName("三国演义");

        // TODO: 2018/7/23 添加消息队列 
        this.rabbitTemplate.convertAndSend(RabbitConfig.DEFAULT_BOOK_QUEUE, book);
        this.rabbitTemplate.convertAndSend(RabbitConfig.MANUAL_BOOK_QUEUE, book);

        // TODO: 2018/7/23 添加延时队列 
        this.rabbitTemplate.convertAndSend(
                RabbitConfig.REGISTER_DELAY_EXCHANGE, RabbitConfig.DELAY_ROUTING_KEY, book, message -> {
                    // TODO: 2018/7/23
                    message.getMessageProperties().setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Book.class.getName());
                    message.getMessageProperties().setExpiration(2 * 1000 + "");
                    return message;
                });

        log.info("[发送时间] - [{}]", LocalDateTime.now());
    }

    /**
     *  @LocalLock(key = "book:ages[0]") : 将args[0] 替换成第一个参数值，生成的新key 将被缓存起来
     *
     * @param token
     * @return
     */
    @LocalLock(key = "book:ages[0]")
    @GetMapping("/query")
    public String query(@RequestParam String token) {
        return "success-" + token;
    }

    /**
     * @CacheParam
     *
     * @param token
     * @return
     */
    @CacheLock(prefix = "books")
    @GetMapping("/get")
    public String get(@CacheParam(name = "token") @RequestParam String token) {
        return "success - " + token;
    }
}
