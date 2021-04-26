package org.feng.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@ContextConfiguration
@SpringBootTest
public class SendMessageTest {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void sends() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("fengQueue", "第" + i + "条消息");
        }
    }

}
