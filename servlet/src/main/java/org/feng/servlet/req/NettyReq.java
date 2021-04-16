package org.feng.servlet.req;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * 使用netty发送http请求
 */
public class NettyReq {

    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException, InterruptedException {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpRequestEncoder());// 客户端对发送的httpRequest进行编码
                            socketChannel.pipeline().addLast(new HttpResponseDecoder());// 客户端需要对服务端返回的httpresopnse解码
//                        socketChannel.pipeline().addLast(new StartTestResponse());
                        }
                    });

            Channel channel = bootstrap.connect("127.0.0.1", 8888).sync().channel();

            URI uri = new URI("/api/echo");
            String content = "fuck";
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST,
                    uri.toASCIIString(),
                    Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
//        request.headers().set(HttpHeaderNames.CONTENT_LANGUAGE, "zh");
            request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);

            request.headers().set(HttpHeaders.Names.HOST, "127.0.0.1");
//            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);

            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}