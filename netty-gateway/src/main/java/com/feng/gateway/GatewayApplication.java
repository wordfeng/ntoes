package com.feng.gateway;

import com.feng.gateway.config.GatewayOptions;
import com.feng.gateway.info.HostInfo;
import com.feng.gateway.info.JVMInfo;
import com.feng.gateway.netty.GatewayServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.PrintWriter;
import java.nio.channels.spi.SelectorProvider;

import static com.feng.gateway.config.GatewayOptions.GATEWAY_OPTION_PARALLEL;

@EnableAsync
@EnableConfigurationProperties
@SpringBootApplication
public class GatewayApplication {
    public static int PORT = 0;
    private static final JVMInfo JVM_INFO = new JVMInfo();
    private static final HostInfo HOST_INFO = new HostInfo();

    private static void dumpSystemInfo(PrintWriter out) {
        out.println();
        out.println("NettyGateway 1.0,Build 2018/4/18,Author:tangjie");
        out.println("https://github.com/tang-jie/NettyGateway");
        out.print(JVM_INFO);
        out.print(HOST_INFO);
        out.println("PORT : " + PORT);
        out.println("NettyGateway start success!\n");
        out.flush();
    }

    public static void main(String[] args) throws Exception {
//		ConfigurableApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
        ApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:netty-gateway.xml");
        PORT = ((GatewayOptions) context.getBean("gatewayOptions")).getGatewayPort();

        dumpSystemInfo(new PrintWriter(System.out));

        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty.gateway.boss"));//, Thread.MAX_PRIORITY
        EventLoopGroup workerGroup = new NioEventLoopGroup(GATEWAY_OPTION_PARALLEL, new DefaultThreadFactory("netty.gateway.worker"));//, Thread.MAX_PRIORITY

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channelFactory(new ChannelFactory<NioServerSocketChannel>() {
                        @Override
                        public NioServerSocketChannel newChannel() {
                            return new NioServerSocketChannel(SelectorProvider.provider());
                        }
                    })
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new GatewayServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(NioChannelOption.SO_KEEPALIVE, true);
            ChannelFuture sync = b.bind(PORT).sync();
            sync.channel().closeFuture().sync();
//            Channel ch = b.bind(PORT).channel();
//            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
