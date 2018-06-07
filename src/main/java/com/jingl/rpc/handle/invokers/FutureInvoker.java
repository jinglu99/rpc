package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.common.exceptions.TimeOutException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.InvocationPool;
import com.jingl.rpc.utils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 将请求调用放入池中，最外层Invoker
 * Created by Ben on 01/04/2018.
 */
public class FutureInvoker implements Invoker {
    private static final Logger logger = Logger.getLogger(FutureInvoker.class);
    private static final long timeout = Long.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_TIMEOUT));

    private final Invoker invoker;

    public FutureInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;

        if (invocation.isInterupt()) {
            return null;
        }

        try {
            InvocationPool.addInvocation(invocation);
            invoker.invoke(invocation);
            Response response = invocation.getQueue().poll(timeout, TimeUnit.MILLISECONDS);
            if (response == null) {
                throw new TimeOutException();
            }
            return response;
        } catch (Exception e) {
            throw new InvokerException(e);
        } finally {
            InvocationPool.removeInvoceation(invocation.getId());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue(1);
        try {
            queue.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
