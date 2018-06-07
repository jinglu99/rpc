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
    private static volatile Invoker nextInvoker;
    private static volatile Invoker afterResponseInvoker;
    private static final int threadNo = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_THREAD));
    private static final ExecutorService executor = Executors.newFixedThreadPool(threadNo);

    public static Invoker getNextInvoker() {
        return nextInvoker;
    }

    public static void setNextInvoker(Invoker nextInvoker) {
        ProviderPool.nextInvoker = nextInvoker;
    }

    public static Invoker getAfterResponseInvoker() {
        return afterResponseInvoker;
    }

    public static void setAfterResponseInvoker(Invoker afterResponseInvoker) {
        ProviderPool.afterResponseInvoker = afterResponseInvoker;
    }

    public static void submit(ChannelHandlerContext ctx, byte[] data) {
        try {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    byte[] response = new byte[0];
                    try {
                        response = (byte[]) nextInvoker.invoke(data);
                        ctx.writeAndFlush(response);
                        if (afterResponseInvoker != null)
                            afterResponseInvoker.invoke(response);
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
