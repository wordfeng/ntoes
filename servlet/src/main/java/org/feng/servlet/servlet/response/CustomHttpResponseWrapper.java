package org.feng.servlet.servlet.response;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;


@Slf4j
public class CustomHttpResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream output;
    private ServletOutputStream servletOutputStream;

    public CustomHttpResponseWrapper(HttpServletResponse response) {
        super(response);
        log.info("进入响应包装器: " + this.getClass().getName());
        output = new ByteArrayOutputStream();
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (servletOutputStream == null) {
            servletOutputStream = new ServletOutputStream() {
                @Override
                public void write(int b) {
                    output.write(b);
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                }
            };
        }
        return servletOutputStream;
    }
}
