package com.jingl.handle.handlers;

import com.jingl.common.exceptions.InvokerException;
import com.jingl.handle.Handler;
import com.jingl.handle.Invoker;
import com.jingl.handle.invokers.ProviderInvoker;

/**
 * Created by Ben on 26/11/2017.
 */
public class ProviderHandle implements Handler {

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invoker invoker = getInvoker(val);
        invoker.invoke(val);


        return null;
    }

    private Invoker getInvoker(Object val) {
        return new ProviderInvoker();
    }
}
