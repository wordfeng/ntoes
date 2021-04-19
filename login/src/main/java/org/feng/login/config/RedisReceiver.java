package org.feng.login.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisReceiver implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("消息：" + new String(message.getBody()));
        log.info("频道：" + new String(message.getChannel()));
    }
}
