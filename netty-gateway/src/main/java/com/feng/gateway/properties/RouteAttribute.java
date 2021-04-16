package com.feng.gateway.properties;

import lombok.Data;

/**
 * 对应netty-route.properties配置文件的数据结构。
 */
@Data
public class RouteAttribute {
    private String serverPath;
    private String keyWord;
    private String matchAddr;

}
