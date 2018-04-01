package com.jingl.netty.sample;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ben on 16/03/2018.
 */
public class Client implements Runnable {
    public static void main(String args[]) throws InterruptedException, IOException {
        Client client = new Client();

        client.export();

        AtomicInteger integer = new AtomicInteger();

        int n = 100;

        CountDownLatch latch = new CountDownLatch(n);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String input = in.readLine();
//            if (! input.equals("quit")) {
//                String rep = client.send(input);
//                System.out.println(rep);
//            } else {
//                client.close();
//                return;
//            }
//        }

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i<n; i++) {
            executorService.submit(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                        int value = integer.getAndAdd(1);
                        latch.countDown();
                        String rep = client.send(String.valueOf(value));
//                        System.out.println(rep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        latch.await();
        System.out.println("end");
    }


    public static ConcurrentHashMap<String, BlockingQueue<Data>> responses = new ConcurrentHashMap<>();
    public static BlockingQueue<Data> requests = new LinkedBlockingQueue<>();
    private CountDownLatch flag = new CountDownLatch(1);
    private static volatile Channel channel;




    Bootstrap b = new Bootstrap();
    EventLoopGroup group = new NioEventLoopGroup(4);

    public Client() {
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(new InetSocketAddress("127.0.0.1", 2532))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());
                        socketChannel.pipeline().addLast(new NettyEncoder());
                    }
                });


    }

    public void export() throws InterruptedException {
        new Thread(this).start();
        flag.await();
    }

    public void close() {
        channel.close();
    }

    public String send(String req) throws InterruptedException {

        String id = UUID.randomUUID().toString();
        BlockingQueue queue = new ArrayBlockingQueue(1);
        responses.put(id, queue);
        Data data = new Data();
        data.code = id;
        data.msg = req;
//        requests.put(data);
//        String json = JSON.toJSONString(data);
        channel.writeAndFlush(data);
//        Data rep = (Data) queue.take();

        responses.remove(id);
//        return rep.msg;
        return null;
    }

    @Override
    public void run() {

        try {
            ChannelFuture f = b.connect().sync();
            channel = f.channel();
            flag.countDown();
            f.channel().closeFuture().sync();
            System.out.println("退出");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler extends ChannelInboundHandlerAdapter {
        private volatile String rep;

        public String getRep() {
            return rep;
        }

        public void setRep(String rep) {
            this.rep = rep;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("connected");
        }

        @Override
        public void channelRead(io.netty.channel.ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            String json = in.toString(CharsetUtil.UTF_8);
            Data rep = JSON.parseObject(json, Data.class);
            BlockingQueue queue = responses.get(rep.code);
            queue.put(rep);
        }


    }
}
