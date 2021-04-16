package org.feng.servlet.servlet.response.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.feng.servlet.servlet.response.CustomHttpResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
//@Order(Ordered.LOWEST_PRECEDENCE - 1)
//@WebFilter(urlPatterns = {"/*"}, filterName = "responseWrapperFilter")
public class HttpResponseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("进入响应过滤器：" + this.getClass().getName());
        log.info("请求URL：" + request.getRequestURL());
        log.info("请求contenttype：" + request.getContentType());
        log.info("请求getContextPath：" + request.getContextPath());

        CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);
        String responseStr = new String(responseWrapper.toByteArray(), response.getCharacterEncoding());
        log.info("原始响应数据：" + responseStr);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 200);
        jsonObject.put("response", responseStr);
        byte[] bytesData = JSON.toJSONBytes(jsonObject);

        response.setContentLength(bytesData.length);
        // 需要根据http accept来设置
        response.setContentType("application/json;charset=utf-8");
        log.info("包装响应数据: " + jsonObject.toString());
        response.getOutputStream().write(bytesData);
    }
}
