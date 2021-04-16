package com.example.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PropConfigTest {

    @Autowired
    PropConfig propConfig;

    @Test
    public void config(){
        System.out.println(propConfig);
    }

}
