package com.zhaopeng.eagle.invoker;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by zhaopeng on 2016/10/31.
 */
public class RpcInvokerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        pipeline.addLast(new InvokerServiceHandler());
    }
}
