package com.jingl.rpc.transfer.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Ben on 29/03/2018.
 */
public class NettyEncoder extends MessageToByteEncoder<byte[]> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, byte[] data, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}

