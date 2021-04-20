package org.feng.persistent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = "org.feng.persistent.dao")
@SpringBootApplication
public class PersistentApp {
    public static void main(String[] args) {
        SpringApplication.run(PersistentApp.class, args);
    }

}
