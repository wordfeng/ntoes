package org.feng.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.feng.utils.QRCodeUtils.QRCodeUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Scope("request")
@RestController
public class QRCodeController {

    @Resource(name = "loginRedis")
    RedisTemplate<String, Object> login;

    /**
     * 获取qrcode
     * base64
     *
     * @return
     */
    @GetMapping("/qrcode")
    byte[] getQRcode(String uuid, HttpServletResponse response) throws IOException {

        log.info("qrcode uuid: " + uuid);
        String content = "http://192.168.53.41:8081/login/login?uuid=" + uuid;
        log.info(content);
        byte[] bytes = QRCodeUtils.writeToPNGBase64(content);
        response.getOutputStream().write(bytes, 0, bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.flush();
        outputStream.close();
        log.info("生成二维码完成");

        return bytes;
    }

    /**
     * 扫描访问
     */
    @GetMapping("/login")
    void scanAccess(String uuid) {
        log.info("scan uuid: " + uuid);
//        login.convertAndSend(Redis.LOGIN_TOPIC, uuid);
        try {
            login.opsForValue().set(uuid, true);
        } catch (Exception e) {
            log.info("scan error");
            e.printStackTrace();
        }
    }


}
