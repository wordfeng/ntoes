package org.feng.servlet.controller;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import com.alibaba.csp.sentinel.slots.block.RuleConstant;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

@SuppressWarnings("all")
@RestController
public class Test {
    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 20; i++) {
            if (i == 10) {
                Thread.sleep(1000);
            }
            System.out.println(new BufferedReader(new InputStreamReader(new URL("http://localhost:8888/y").openStream())).lines().collect(Collectors.joining()));
        }

//        Thumbnails.of(new File("/Users/aegon/Pictures/壁纸/t01b1f5081b9c0acee1.jpg"))
//                .size(160, 160)
//                .rotate(90)
////                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("watermark.png")), 0.5f)
//                .outputQuality(0.8f)
//                .toFile(new File("/Users/aegon/Pictures/壁纸/thumbnail.jpg"));
    }

//    static {
//        List<FlowRule> rules = new ArrayList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource("date");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        // Set limit QPS to 20.
//        rule.setCount(5);
//        rules.add(rule);
//        FlowRuleManager.loadRules(rules);
//    }

//    private static final RateLimiter limiter = RateLimiter.create(5); //guava

    @RequestMapping("/qq")
    public String qq() {
//        ArrayBlockingQueue
//        new Disruptor()
        return "1" + "2";
    }

    private static AtomicLong counter = new AtomicLong(0);
    //    @SentinelResource(value = "date", resourceType = RuleConstant.FLOW_GRADE_QPS)
    @RequestMapping("/y")
    public String date() {
        System.out.println(counter.getAndAdd(1));
//        System.out.println(LocaleContextHolder.getLocale());
//        System.out.println(RequestContextHolder.getRequestAttributes().getSessionId());
//        System.out.println(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse());
//        System.out.println(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String solarCalendar = dateTimeFormatter.format(LocalDateTime.now());


       /* guava
        double acquire = 1;
        if ((acquire = limiter.acquire()) != 0.0) {
            return "拒绝访问";
        }
        System.out.println(String.format("time: %s; acquire: %s", solarCalendar, acquire));*/
//        Arrays.sort();
        DateTime dateTime = DateUtil.parseDate(solarCalendar);
        ChineseDate chineseDate = new ChineseDate(dateTime);
        String format = String.format("公历: %s<br/>农历: %s<br/>天干地支: %s<br/>" + new Random().nextInt(), solarCalendar, chineseDate, chineseDate.getCyclicalYMD());
        return format;
    }

    @GetMapping("getPicsData")
    public String getpics() {
        return "{current:1" +
                ",page:1," +
                "data:[" +
                "{\n" +
                "          id: \"d2a2fc03cb674c6bbe487c60a69b1078\",\n" +
                "          imgUrl: \"/img/chart/slide-layout/sample/sample_5.png\",\n" +
                "          link: {\n" +
                "            page: {\n" +
                "              pageId: \"\",\n" +
                "              pageType: PageType.Link,\n" +
                "              pageName: \"\",\n" +
                "            },\n" +
                "            openMode: OpenMode.Default,\n" +
                "            winSize: \"60%\",\n" +
                "          },\n" +
                "          title: \"\",\n" +
                "          description: \"\",\n" +
                "        }]}";
    }

    @PostMapping("/echo")
    public String echo(Object word) {
        System.out.println("word: " + (String) word);
        return word + " x";
    }

    @GetMapping("/echo")
    public String ech(Object word) {
        System.out.println(RequestContextHolder.getRequestAttributes());
        System.out.println("get word: " + (String) word);
        return word + " x";
    }


}
