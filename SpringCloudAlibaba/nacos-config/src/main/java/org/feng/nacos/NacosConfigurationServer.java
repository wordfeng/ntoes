package org.feng.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NacosConfigurationServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NacosConfigurationServer.class)
                .run(args);
    }

}
