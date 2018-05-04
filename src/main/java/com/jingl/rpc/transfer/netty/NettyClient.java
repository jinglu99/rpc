package com.jingl.rpc.transfer.netty;

import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;
import com.jingl.rpc.handle.Invoker;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Ben on 26/03/2018.
 */
public class NettyClient {
    Logger logger = Logger.getLogger(NettyClient.class);

    private Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private volatile URL url;
    private final CountDownLatch flag = new CountDownLatch(1);
    private Channel channel;
    private volatile Invoker invoker;

    public byte[] send(byte[] data) {
        ByteBuf buf = null;
        try {
            flag.await();
            channel.writeAndFlush(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }


        return null;
    }

    public int connect() {
        if (channel == null) {
            synchronized (this) {
                if (channel == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }).start();
                }
            }
        }
        return 0;
    }

    public int close() throws SocketCloseFailedException {
        channel.close();
        return 0;
    }

    public void setParams(URL url, Invoker invoker) throws ConnectionFailedException {
        if (this.url == null || this.invoker == null) {
            synchronized (this) {
                if (this.url == null) {
                    this.url = url;
                    this.invoker = invoker;
                }
            }
        }
    }

    private void doConnect() {
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(new InetSocketAddress(url.getIp(), url.getPort()))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyEncoder());
                            socketChannel.pipeline().addLast(new NettyDecoder());
                            socketChannel.pipeline().addLast(new NettyClientHandler(invoker));
                        }
                    });
            ChannelFuture f = bootstrap.connect().sync();
            channel = f.channel();
            flag.countDown();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("can't connect to the provider: " + url.toString());
            e.printStackTrace();
        }

    }
}
