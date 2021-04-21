package org.feng.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    @LoadBalanced // 使RestTemplate对象自动支持Ribbon负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
