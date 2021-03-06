package com.zhaopeng.remote.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zhaopeng on 2016/11/4.
 */
public class Encoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public Encoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }
    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = ProtostuffUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
