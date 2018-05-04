package com.jingl.rpc.netty.sample;

import com.jingl.rpc.serializer.FastjsonRPCSerializer;
import com.jingl.rpc.serializer.RPCSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Ben on 29/03/2018.
 */
public class NettyEncoder extends MessageToByteEncoder<Data> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Data data, ByteBuf byteBuf) throws Exception {
        RPCSerializer serializer = new FastjsonRPCSerializer();

        byte[] bytes = serializer.serialize(data);

        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        for (byte b : bytes) {
            System.out.print(b);
            System.out.print(" ");
        }
        System.out.println();
    }
}
