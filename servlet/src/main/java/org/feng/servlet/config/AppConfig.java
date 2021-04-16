package org.feng.servlet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration(value = "q")
@ComponentScan
public class AppConfig {
//    @Bean(name = "qqq")
    public ZoneId createZone0fz() {
        return ZoneId.of("za");
    }

//    @Bean(name = "qq")
    public ZoneId createZoneOfUTC8() {
        return ZoneId.of("UTC+08:00");
    }
}