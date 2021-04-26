package org.feng.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;


@Slf4j
@SpringBootApplication
public class MessageServer {

    public static void main(String[] args) {
        SpringApplication.run(MessageServer.class, args);
    }

//    @Bean
//    SimpleMessageListenerContainer simpleMessageListenerContainer(){
//
//    }

    @Order(Integer.MAX_VALUE)
    @Bean(name = "test")
    String test(){
        return "test";
    }

}
