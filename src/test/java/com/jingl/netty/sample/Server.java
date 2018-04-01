package com.jingl.netty.sample;


import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * Created by Ben on 16/03/2018.
 */
public class Server {
    public static void main(String args[]) throws Exception {
        Server server = new Server();
        server.run();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0 ,4));
                            socketChannel.pipeline().addLast(new HelloServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = bootstrap.bind(2532).sync();

            System.out.println("server running");

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class HelloServerHandler extends
            ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(io.netty.channel.ChannelHandlerContext ctx) throws Exception {


            System.out.println("connected");
        }

        @Override
        public void channelRead(io.netty.channel.ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf req = (ByteBuf) msg;
            int l = req.readableBytes();

            byte[] bytes = new byte[l];

            req.readBytes(bytes);

            for (byte b : bytes) {
                System.out.print(b);
                System.out.print(" ");
            }
            System.out.println();
//            System.out.println("received: " + json);
//            Data req = JSON.parseObject(json, Data.class);
//            req.msg = "server received: " + req.msg;

//            System.out.println(req.msg);
//            String rep = JSON.toJSONString(req);
//            ByteBuf buf = Unpooled.copiedBuffer(rep, CharsetUtil.UTF_8);
//            ctx.writeAndFlush(buf);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }

    }

}
