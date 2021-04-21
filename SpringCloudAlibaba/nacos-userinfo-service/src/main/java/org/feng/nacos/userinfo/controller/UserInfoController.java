package org.feng.nacos.userinfo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    RestTemplate template;

    @RequestMapping("/{id}")
    public String info(@PathVariable int id) {

        String name = template.getForObject(String.format("http://username-service/username/%s", id), String.class);

        return String.format("id: %s, name: %s", id, name);
    }

}
