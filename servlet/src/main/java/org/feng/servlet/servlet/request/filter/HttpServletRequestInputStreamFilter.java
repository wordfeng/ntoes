package org.feng.servlet.servlet.request.filter;

import org.feng.servlet.servlet.request.InputStreamHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class HttpServletRequestInputStreamFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("进入请求过滤器：" + this.getClass().getName());
        ServletRequest servletRequest = new InputStreamHttpServletRequestWrapper(httpServletRequest);
        filterChain.doFilter(servletRequest, httpServletResponse);
    }
}
