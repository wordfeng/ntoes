package org.feng.message.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageReceiver {

    /**
     * 被监听队列不存在时需要手动创建
     */
    @RabbitListener(queues = "fengQueue")
    public void process(String message) {
        log.info(this.getClass().getName() + " fengQueue " + message);
    }

    /**
     * 被监听队列不存在时自动创建
     */
    @RabbitListener(queuesToDeclare = @Queue("feng"))
    public void process1(String message) {
        log.info( "{} queue: feng message: {}" ,this.getClass().getName(), message);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("muhua"),
            exchange = @Exchange("muhuaExchange"),
            key = "lalala"  // 指定路由的key
    ))
    public void fengProcessor(String message) {
        log.info("Queue:muhua, Exchange: muhuaExchange, key: lalala, message: {}", message);
    }

}
