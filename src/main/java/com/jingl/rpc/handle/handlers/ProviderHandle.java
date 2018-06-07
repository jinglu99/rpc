package com.jingl.rpc.handle.handlers;

import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Handler;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.ProviderInvoker1;

/**
 * Created by Ben on 26/11/2017.
 */
public class  ProviderHandle implements Handler {

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invoker invoker = getInvoker(val);
        invoker.invoke(val);


        return null;
    }

    private Invoker getInvoker(Object val) {
        return new ProviderInvoker1();
    }
}
