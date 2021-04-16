package org.feng.servlet.servlet.request;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.Servlet;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class InputStreamHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] requestBody;
    //    private static final int BUFFER_SIZE = 8192;
    private final HttpServletRequest request;


    public InputStreamHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        log.info("进入请求包装类：" + this.getClass().getName());
        this.requestBody = ByteStreams.toByteArray(request.getInputStream());
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
