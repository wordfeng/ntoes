package com.feng.gateway.netty;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


public class GatewayServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
//        p.addLast(new HttpObjectAggregator(3096));
        p.addLast(new HttpRequestDecoder());//把请求拆分成HttpRequest和HttpContent两部分   建立了两次连接
        p.addLast(new HttpResponseEncoder());
        p.addLast(new GatewayForwardingHandler());
    }
}