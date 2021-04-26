package org.feng.message.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageSender {
    @Autowired
    private AmqpTemplate rabbitmqTemplate;
    static int sendCounter = 0;

    @RequestMapping("/send/{message}")
    public String send(@PathVariable String message) {
        rabbitmqTemplate.convertAndSend("feng", message + " " + sendCounter++);
        return message;
    }

}
