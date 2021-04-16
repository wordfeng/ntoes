package org.feng.servlet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = {"classpath:/application.properties"}, encoding = "UTF-8")
@ConfigurationProperties("server")
@Component(value = "qwer")
public class PropConfig {
    public String port;
    public String servletContextPath;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "PropConfig{" +
                "port='" + port + '\'' +
                '}';
    }
}