package com.jingl.rpc.transfer.netty;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.exceptions.SendDataFailedException;
import com.jingl.rpc.common.exceptions.ServiceExportFailedException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.ExportTransfer;
import com.jingl.rpc.utils.Peppa;
import com.jingl.rpc.utils.PropertyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;


/**
 * Created by Ben on 31/03/2018.
 */
public class NettyExportTransfer implements ExportTransfer {
    private static Logger logger = Logger.getLogger(NettyExportTransfer.class);

    private Bootstrap bootstrap;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile Invoker invoker;
    private volatile Channel channel;

    @Override
    public byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException {
        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }

        channel.writeAndFlush(data);
        return null;
    }

    @Override
    public int export() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doExport();
                } catch (InterruptedException e) {
                    logger.error("provider disconnected");
                }
            }
        }).start();
        return 0;
    }

    @Override
    public boolean isActive() {
        if (channel == null) return false;
        return channel.isActive();
    }

    @Override
    public int close() throws SocketCloseFailedException {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
        return 0;
    }

    @Override
    /**
     * 设置参数
     * port 无效
     */
    public void setParams(int port, Invoker invoker) throws ServiceExportFailedException {
        if (this.invoker == null) {
            synchronized (this) {
                if (this.invoker == null) {
                    this.invoker = invoker;
                }
            }
        }

    }

    private void doExport() throws InterruptedException {
        //获得监听端口
        int port = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_PORT));

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyDecoder());
                        socketChannel.pipeline().addLast(new NettyEncoder());
                        socketChannel.pipeline().addLast(new NettyServerHandler(invoker));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture f = bootstrap.bind(port).sync();
        this.channel = f.channel();
        latch.countDown();
        Peppa.draw();
        logger.info("provider started, bind port: " + port);
        f.channel().closeFuture().sync();
    }
}
