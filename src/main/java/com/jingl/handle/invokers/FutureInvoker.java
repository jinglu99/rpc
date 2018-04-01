package com.jingl.handle.invokers;

import com.jingl.common.entity.Invocation;
import com.jingl.common.exceptions.InvokerException;
import com.jingl.handle.Invoker;
import com.jingl.pools.InvocationPool;
import org.apache.log4j.Logger;

/**
 * Created by Ben on 01/04/2018.
 * 将请求调用放入池中，最外层Invoker
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
            return invoker.invoke(invocation);
        } finally {
            InvocationPool.removeInvoceation(invocation.getId());
        }
    }
}
