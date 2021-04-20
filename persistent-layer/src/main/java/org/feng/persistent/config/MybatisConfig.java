package org.feng.persistent.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    @Bean
    public SqlInterceptor sqlInterceptor() {
        return new SqlInterceptor();
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer(SqlInterceptor sqlInterceptor) {
//        return new ConfigurationCustomizer() {
//            @Override
//            public void customize(org.apache.ibatis.session.Configuration configuration) {
//                configuration.setMapUnderscoreToCamelCase(true);
//                configuration.addInterceptor(sqlInterceptor);
//            }
//        };
//    }

}

