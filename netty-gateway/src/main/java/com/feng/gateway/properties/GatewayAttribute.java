package com.feng.gateway.properties;


import lombok.Data;

/**
 * 对应netty-gateway.properties配置文件的数据结构。
 */
@Data
public class GatewayAttribute {
    private String serverPath;
    private String defaultAddr;
}
