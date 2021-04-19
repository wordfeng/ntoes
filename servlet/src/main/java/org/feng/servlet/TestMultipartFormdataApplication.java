package org.feng.servlet;

import org.feng.servlet.config.PropConfig;
import org.feng.servlet.config.mvc.MVCConfigurger;
import org.feng.servlet.servlet.request.filter.HttpServletRequestInputStreamFilter;
//import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.annotation.MapperScans;
import lombok.extern.slf4j.Slf4j;
import org.feng.servlet.servlet.response.filter.HttpResponseFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


//@ServletComponentScan(basePackages = "org.feng.servlet.servlet.response.filter")
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class TestMultipartFormdataApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TestMultipartFormdataApplication.class, args);
        PropConfig p = (PropConfig) run.getBean("qwer");
        System.out.println(p);
    }

    @Value("${server.port}")
    String port;

    @Bean(name = "test1")
    public String test1() {
        return "123";
    }

    @Bean//(name = "test2")
    public String test2() {
        return "456";
    }

    /**
     * todo 排除静态资源
     * @return
     */
    @Order(1)
    @Bean
    public FilterRegistrationBean<HttpServletRequestInputStreamFilter> inputStreamWrapperFilter() {//@Qualifier(value = "test2") String test
        log.info("加载请求过滤器...");
        final FilterRegistrationBean<HttpServletRequestInputStreamFilter> frb = new FilterRegistrationBean<>();
        frb.setFilter(new HttpServletRequestInputStreamFilter());
        frb.setName("InputStreamFilter4Wrapper");
        frb.addUrlPatterns("/*");
        return frb;
    }

    /**
     * 这种方式需要自己写类型判断, 排除静态资源，使用{@link MVCConfigurger#addReturnValueHandlers}替代
     * server.servlet.context-path不在匹配串里，规则需要去掉前缀
     */
//    @Order(Ordered.LOWEST_PRECEDENCE - 1)
//    @Bean
//    public FilterRegistrationBean<HttpResponseFilter> outputStreamWrapperFilter() {
//        log.info("加载响应过滤器...");
//        final FilterRegistrationBean<HttpResponseFilter> frb = new FilterRegistrationBean<>();
//        frb.setFilter(new HttpResponseFilter());
//        frb.setName("OutputStreamFilter4Wrapper");
//        frb.addUrlPatterns("/*");
//        return frb;
//    }
}
