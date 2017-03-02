package com.zhaopeng.eagle.invoker;


import com.zhaopeng.eagle.entity.Request;

import com.zhaopeng.eagle.entity.Response;
import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.protocol.Decoder;
import com.zhaopeng.eagle.protocol.Encoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * Created by zhaopeng on 2016/10/31.
 */
public class InvokerInitializerChannel extends ChannelInitializer<SocketChannel>{



    private URL url;

    public InvokerInitializerChannel(){

    }

    public InvokerInitializerChannel(URL url) {
        this.url = url;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
        pipeline.addLast(new Encoder(Request.class));
        pipeline.addLast(new Decoder(Response.class));
        pipeline.addLast(new InvokerServiceHandler(url));
    }
}
