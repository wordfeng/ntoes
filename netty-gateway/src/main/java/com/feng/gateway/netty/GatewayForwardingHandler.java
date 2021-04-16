package com.feng.gateway.netty;

import com.feng.gateway.commons.RoutingLoader;
import com.feng.gateway.properties.GatewayAttribute;
import com.feng.gateway.properties.RouteAttribute;
import com.feng.gateway.utils.HttpClientUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.Signal;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.feng.gateway.config.GatewayOptions.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 关键字过滤转发代码模块，主要完成路由转发地址的匹配计算、路由转发、以及应答转发结果给请求客户端的工作。
 */
public class GatewayForwardingHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private StringBuilder buffer = new StringBuilder();
    private String url = "";
    private String uri = "";
    private StringBuilder respone;
    private static AtomicLong counter = new AtomicLong(0);
    //    private static GlobalEventExecutor executor = GlobalEventExecutor.INSTANCE;
    private GlobalEventExecutor executor = GlobalEventExecutor.INSTANCE;
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        System.out.println("count: "+counter.getAndAdd(1));
//        System.out.println(Thread.currentThread().getName());
        if (msg instanceof HttpRequest) {
//            System.out.println("HttpRequest");
            HttpRequest request = this.request = (HttpRequest) msg;

            //收到客户端的100-Continue协议请求，说明客户端要post数据给服务器
            if (HttpUtil.is100ContinueExpected(request)) {
                notify100Continue(ctx);
            }

            buffer.setLength(0);
            uri = request.uri().substring(1);
        }

        if (msg instanceof HttpContent) {
//            System.out.println("HttpContent");
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
//                System.out.println("content.toString(GATEWAY_OPTION_CHARSET): " + content.toString(GATEWAY_OPTION_CHARSET));
                buffer.append(content.toString(GATEWAY_OPTION_CHARSET));
            }

            //获取post数据完毕
            if (msg instanceof LastHttpContent) {
                LastHttpContent trace = (LastHttpContent) msg;

                System.out.println("[NETTY-GATEWAY] REQUEST : " + buffer.toString());

                //根据netty-gateway.properties、netty-route.properties匹配出最终转发的URL地址
                url = matchUrl();
                System.out.println("[NETTY-GATEWAY] URL : " + url);

                //http请求异步转发处理，不要阻塞当前的Netty Handler的I/O线程，提高服务器的吞吐量。
                Future<StringBuilder> future = executor.submit(new Callable<StringBuilder>() {
                    @Override
                    public StringBuilder call() {
//                        System.out.println("start post ");
                        return HttpClientUtils.post(url, buffer.toString(), GATEWAY_OPTION_HTTP_POST);
                    }
                });

                future.addListener(new FutureListener<StringBuilder>() {
                    @Override
                    public void operationComplete(Future<StringBuilder> future) throws Exception {
                        if (future.isSuccess()) {
                            respone = ((StringBuilder) future.get(GATEWAY_OPTION_HTTP_POST, TimeUnit.MILLISECONDS));
                            System.out.println("success response: " + respone);
                        } else {
                            respone = new StringBuilder(((Signal) future.cause()).name());
                            System.out.println("fail response: " + respone);
                        }
                        latch.countDown();
//                        System.out.println("countDown");
                    }
                });

                try {
                    latch.await();
//                    System.out.println("await");
                    writeResponse(respone, future.isSuccess() ? trace : null, ctx);
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    //根据netty-gateway.properties、netty-route.properties匹配出最终转发的URL地址
    private String matchUrl() {
        for (GatewayAttribute gateway : RoutingLoader.GATEWAYS) {
            if (gateway.getServerPath().equals(uri)) {
                for (RouteAttribute route : RoutingLoader.ROUTERS) {
                    if (route.getServerPath().equals(uri)) {
                        String[] keys = StringUtils.delimitedListToStringArray(route.getKeyWord(), GATEWAY_OPTION_KEY_WORD_SPLIT);
                        boolean match = true;
                        for (String key : keys) {
                            if (key.isEmpty()) continue;
                            if (!buffer.toString().contains(key.trim())) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            return route.getMatchAddr();
                        }
                    }
                }

                return gateway.getDefaultAddr();
            }
        }
        return null;//GATEWAY_OPTION_LOCALHOST;
    }

    //把路由转发的结果应答给http客户端
    private void writeResponse(StringBuilder respone, HttpObject current, ChannelHandlerContext ctx) {
        if (respone != null) {
            boolean keepAlive = HttpUtil.isKeepAlive(request);
//            System.out.println("keepAlive: " + keepAlive);

            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, current == null ? OK : current.decoderResult().isSuccess() ? OK : BAD_REQUEST,
                    Unpooled.copiedBuffer(respone.toString(), GATEWAY_OPTION_CHARSET));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=" + GATEWAY_OPTION_CHARSET);

            if (keepAlive) {
                response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                //close keep-alive
//                HttpUtil.setKeepAlive(response.headers(), HTTP_1_1, false);
            }

            ctx.write(response);
        }
    }

    private static void notify100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }
}