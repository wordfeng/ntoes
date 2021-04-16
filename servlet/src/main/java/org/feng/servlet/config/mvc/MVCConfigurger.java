package org.feng.servlet.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MVCConfigurger implements WebMvcConfigurer {

    String[] list = {"/**/*.html", "/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.svg", "/**/*.woff2"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new URLHandleInterceptor())
                .excludePathPatterns(list)
                .addPathPatterns("/**");
    }

    /**
     * 静态资源地址映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.*")
                .addResourceLocations("classpath:/static/home/");
    }

    /**
     * 无效,使用{@link org.feng.servlet.config.mvc.InitJsonifyReturnValueBean}替代
     * */
//    @Override
//    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
//        handlers.add(new JsonifyReturnValue());
//    }
}
