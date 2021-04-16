package org.feng.image.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.feng.image.dto.TestDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(TestDTO params) {
        JSONObject jsonObject = JSON.parseObject(params.toString());
        return "hello k8s: "+jsonObject.toString();
    }
}
