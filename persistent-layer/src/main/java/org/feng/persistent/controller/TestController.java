package org.feng.persistent.controller;

import org.feng.persistent.entity.Test;
import org.feng.persistent.entity.UserInfo;
import org.feng.persistent.holder.UserContextHolder;
import org.feng.persistent.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("all")
    public List<Test> selectOne(String tenant) {
        //方便验证问题 实际不能在这里这样做
        UserInfo userInfo = new UserInfo();
        userInfo.setTenant(tenant);
        UserContextHolder.set(userInfo);
        return this.testService.queryAllByLimit(0, 100);
    }

}