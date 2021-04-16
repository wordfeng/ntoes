package org.feng.servlet.config.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JsonifyReturnValue implements HandlerMethodReturnValueHandler {
    //    private RequestResponseBodyMethodProcessor target;
//
//    public JsonifyReturnValue(RequestResponseBodyMethodProcessor target) {
//        this.target = target;
//    }
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        //api return
        boolean b = returnType.hasMethodAnnotation(RequestMapping.class);
        log.info("supportsReturnType: " + b);
        return b;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        log.info("handleReturnValue: " + returnValue);
        mavContainer.setRequestHandled(true);
        webRequest
                .getNativeResponse(HttpServletResponse.class)
                .getWriter()
                .write(formatReturnValue(returnValue));

    }

    private String formatReturnValue(Object returnValue) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 200);
        jsonObject.put("response", returnValue);
        return jsonObject.toString();
    }

//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        return target.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
//    }
}
