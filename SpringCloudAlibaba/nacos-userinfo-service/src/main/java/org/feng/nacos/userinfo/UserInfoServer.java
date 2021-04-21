package org.feng.nacos.userinfo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UserInfoServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(UserInfoServer.class).run(args);
    }

}
