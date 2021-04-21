package org.feng.register.provider;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProviderServer {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ProviderServer.class).run(args);
    }
}
