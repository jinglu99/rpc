package com.jingl.handle;

import com.jingl.common.exceptions.InvokerException;

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
