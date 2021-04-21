package org.feng.register.provider.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("username")
public class RegisterController {

    @Value("${server.port}")
    String port;

    String[] infos = {"shit", "shit1", "shit2", "shit3"};

    @GetMapping("{id}")
    public String name(@PathVariable int id) {
        if (id > 3 || id < 0) {
            id = 0;
        }
        return infos[id];
    }

}
