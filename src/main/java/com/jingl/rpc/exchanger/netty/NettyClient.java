package com.jingl.rpc.exchanger.netty;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.utils.PropertyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ben on 26/03/2018.
 */
public class NettyClient {
    Logger logger = Logger.getLogger(NettyClient.class);

    private Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private volatile URL url;
    private final CountDownLatch flag = new CountDownLatch(1);
    private final CountDownLatch tried = new CountDownLatch(1);
    private Channel channel;
    private volatile Invoker invoker;
    private volatile boolean closed;
    private volatile boolean dead = false;
    private final int retry = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_NETTY_RETRY));
    private final AtomicInteger currentRetry = new AtomicInteger(0);
    private final int interval = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_NETTY_INTERVAL));

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

    public int connect() throws ConnectionFailedException {
        if (channel == null) {
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

            synchronized (this) {
                final Exception[] exception = {null};
                if (channel == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                doConnect();
                            } catch (ConnectionFailedException e) {
                                exception[0] = e;
                            }
                        }
                    }).start();
                }

                try {
                    tried.await();
                    if (exception[0] != null) throw (ConnectionFailedException)exception[0];
                } catch (InterruptedException e) {
                    throw new ConnectionFailedException(e);
                }
            }
        }
        return 0;
    }

    public boolean isActive() {
        if (channel == null) return false;
        return channel.isActive();
    }

    public boolean isDead() {
        return dead;
    }

    public int close() throws SocketCloseFailedException {
        if (isActive()) {
            channel.close();
            closed = true;
        }
        return 0;
    }

    public void setParams(URL url, Invoker invoker) {
        if (this.url == null || this.invoker == null) {
            synchronized (this) {
                if (this.url == null) {
                    this.url = url;
                    this.invoker = invoker;
                }
            }
        }
    }



    private void doConnect() throws ConnectionFailedException {
        while (!this.closed && currentRetry.get() < retry) {
            try {
                logger.info("try to connect to " + url.getProtocol() + "://" +url.getHost() + ":" + url.getPort());
                tryConnect();
            } catch (Exception e) {
                tried.countDown();
                if (isActive()) channel.close();
                logger.error("can't connect to the provider: " + url.toString());
                e.printStackTrace();
            }

            if (this.closed) {  //退出
                dead = true;
                return;
            }

            //重连前等待间隔
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        //重试失败，判断为失效
        dead = true;
    }

    private void tryConnect() throws InterruptedException {
            currentRetry.addAndGet(1);
            ChannelFuture f = bootstrap.connect().sync();
            channel = f.channel();
            tried.countDown();
            flag.countDown();
            currentRetry.set(0);    //重试计数器归零
            f.channel().closeFuture().sync();
    }
}
