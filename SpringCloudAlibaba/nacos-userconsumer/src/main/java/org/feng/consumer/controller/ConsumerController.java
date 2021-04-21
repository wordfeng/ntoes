package org.feng.consumer.controller;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/userinfo")
public class ConsumerController {

    @LoadBalanced
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/{id}")
    public String userinfo(@PathVariable int id) {
        // 关键点：将原有IP:端口替换为服务名，RestTemplate便会在通信前自动利用Ribbon查询可用provider-service实例列表
        // 再根据负载均衡策略选择节点实例
        return restTemplate.getForObject(String.format("http://userinfo-service/user/%s", id), String.class);
    }
}