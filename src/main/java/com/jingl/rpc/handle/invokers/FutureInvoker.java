package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.InvocationPool;
import org.apache.log4j.Logger;

/**
 * 将请求调用放入池中，最外层Invoker
 * Created by Ben on 01/04/2018.
 */
public class FutureInvoker implements Invoker {
    private static final Logger logger = Logger.getLogger(FutureInvoker.class);

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
            Response response = invocation.getQueue().take();
            if (!response.isSuccess()) {
                throw new InvokerException(response.getException());
            }
            return response.getResponse();
        } catch (InterruptedException e) {
            throw new InvokerException(e);
        } finally {
            InvocationPool.removeInvoceation(invocation.getId());
        }
    }
}
