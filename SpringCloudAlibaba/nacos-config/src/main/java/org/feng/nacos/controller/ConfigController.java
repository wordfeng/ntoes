package org.feng.nacos.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
//刷新配置
@RefreshScope
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Value("${pingpong:null}")
    private String pingpong;


    @RequestMapping("/cache")
    public boolean get() {
        return useLocalCache;
    }

    @RequestMapping("/pingpong")
    public String pingpong() {
        return pingpong;
    }


    @Value("${config1: null}")
    private String config1;
    @Value("${config2: null}")
    private String config2;

    @RequestMapping("/configs")
    public String configs() {
        return config1 + " : " + config2;
    }
}