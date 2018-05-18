package com.jingl.rpc.transfer.netty;

import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.ProviderPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Ben on 25/03/2018.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Invoker invoker;

    private final ProviderPool providerPool;

    public NettyServerHandler(Invoker invoker) {
        this.invoker = invoker;
        this.providerPool = new ProviderPool(this.invoker);
    }

    @Override
    public void channelActive(io.netty.channel.ChannelHandlerContext ctx) throws Exception {
        System.out.println("connected");
    }

    @Override
    public void channelRead(io.netty.channel.ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg;
            int length = ((ByteBuf) msg).readableBytes();
            byte[] data = new byte[length];

            buf.readBytes(data);
            buf.release();
            providerPool.submit(ctx, data);
        } catch (Exception e) {}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
