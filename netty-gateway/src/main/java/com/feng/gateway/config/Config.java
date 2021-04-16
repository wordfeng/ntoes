package com.feng.gateway.config;

import com.feng.gateway.commons.RoutingLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean("gatewayOptions")
    GatewayOptions options(){
        GatewayOptions gatewayOptions = new GatewayOptions();
        gatewayOptions.setGatewayPort(8999);
        return gatewayOptions;
    }
    @Bean
    RoutingLoader routingLoader(){
        return new RoutingLoader();
    }
}
