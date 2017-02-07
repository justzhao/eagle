package com.zhaopeng.eagle.provider;


import com.zhaopeng.eagle.entity.Request;

import com.zhaopeng.eagle.entity.Response;
import com.zhaopeng.eagle.protocol.Decoder;
import com.zhaopeng.eagle.protocol.Encoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by zhaopeng on 2016/10/29.
 */
public class RpcProvideInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast( new LengthFieldBasedFrameDecoder(65536,0,4,0,0));

        pipeline.addLast(new Decoder(Request.class));

        pipeline.addLast(new Encoder(Response.class));

        pipeline.addLast(new ProviderServiceHandler());
    }
}
