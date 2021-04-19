package org.feng.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
//@Scope("request")
@RestController
public class QRcodeWaitController {

    @Resource(name = "loginRedis")
    RedisTemplate<String, Object> login;


    /**
     * 请求等待扫描
     */
    @GetMapping("/wait")
    boolean wait(String uuid) throws InterruptedException {
        log.info("wait uuid: " + uuid);
        long start = System.currentTimeMillis();
        AtomicBoolean status = new AtomicBoolean(false);
        CountDownLatch count = new CountDownLatch(1);
        new Thread(() -> {
            while (System.currentTimeMillis() - start < 1000 * 10) {
                try {
                    Boolean o = Boolean.parseBoolean((String) login.opsForValue().get(uuid));
                    if (o == null) {
                        Thread.sleep(1000);
                    } else if (o) {
                        status.set(true);
                        log.info("成功");
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            count.countDown();
        }).start();
        count.await();
        log.info("login: " + status.get());
        return status.get();
    }

}
