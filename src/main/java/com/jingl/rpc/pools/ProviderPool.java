package com.jingl.rpc.pools;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.utils.PropertyUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Ben on 2018/5/10.
 */
public class ProviderPool {
    private final Invoker nextInvoker;
    private static final int threadNo = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_THREAD));
    private static final ExecutorService executor = Executors.newFixedThreadPool(threadNo);

    public ProviderPool(Invoker invoker) {
        this.nextInvoker = invoker;
    }

    public void submit(ChannelHandlerContext ctx, byte[] data) {
        try {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    byte[] response = new byte[0];
                    try {
                        response = (byte[]) nextInvoker.invoke(data);
                        ctx.writeAndFlush(response);
                    } catch (InvokerException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
