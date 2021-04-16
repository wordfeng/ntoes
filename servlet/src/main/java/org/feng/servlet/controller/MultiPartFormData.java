package org.feng.servlet.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/form")
public class MultiPartFormData {

    @GetMapping("/test")
    public String test() {
        return "{x:qwer}";
    }

    private static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/sub")
    public String form(HttpServletRequest request) throws IOException {
        final String name = request.getParameter("name");
        final ServletInputStream inputStream = request.getInputStream();
        byte[] bytes = new byte[99999];

        inputStream.read(bytes, 0, bytes.length);

        log.error("read first times: " + new String(bytes));
//        bytes = null;
        String s2 = new BufferedReader(new InputStreamReader(request.getInputStream())).lines().collect(Collectors.joining());
        log.error("read second times: " + s2);

        System.out.println(request.getParameter("name"));
        inputStream.close();
        System.out.println(new String(bytes));
        return String.format("name=%s", name);
    }


    @GetMapping("/yf")
    public String xxx() throws ExecutionException, InterruptedException, IOException {
        try {
            ExecutorService e = Executors.newFixedThreadPool(1);
            URL url = new URL("https://api.lovelive.tools/api/SweetNothings/:count/Serialization/:serializationType");
//        Callable<String> c = () -> new BufferedReader(
//                new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"))
//                .lines()
//                .collect(Collectors.joining("\n")).replaceAll("\\{\"code\":200,\"message\":\"\",\"returnObj\":\\[\"", "")
//                .replaceAll("\"]}", "");
//        FutureTask<String> t = new FutureTask<>(c);
//        t.get();
            return new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"))
                    .lines()
                    .collect(Collectors.joining("\n")).replaceAll("\\{\"code\":200,\"message\":\"\",\"returnObj\":\\[\"", "")
                    .replaceAll("\"]}", "");
        }catch (Exception e){
            return e.toString();
        }
    }

}
