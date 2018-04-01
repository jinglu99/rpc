package com.jingl.netty.sample;

import com.jingl.serializer.FastjsonRPCSerializer;
import com.jingl.serializer.RPCSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Ben on 29/03/2018.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final int HEADER_SIZE = 4;
    public NettyDecoder() {
        super(1024*1024*1024, 0, 4,0,4);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        int h = in.readableBytes();
        if (h < HEADER_SIZE) {
            throw new Exception("可读信息段比头部信息都小，你在逗我？");
        }

        int length = in.readInt();

        int l = in.readableBytes();
        if (l < length) {
            in.skipBytes(l);
            throw new Exception("body字段你告诉我长度是"+length+",但是真实情况是没有这么多，你又逗我？");
        }
        ByteBuf buf = in.readBytes(length);
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        l = in.readableBytes();

        RPCSerializer serializer = new FastjsonRPCSerializer();
        Data data = serializer.deserialize(req, Data.class);



        for (byte b : req) {
            System.out.print(b);
            System.out.print(" ");
        }
        System.out.println();

        return data;
    }
}
