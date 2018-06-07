package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.exchanger.Exchanger;
import com.jingl.rpc.handle.Invoker;

/**
 * Created by Ben on 01/04/2018.
 */
public class ExchangeInvoker implements Invoker {
    private final Invoker next;

    public ExchangeInvoker(Invoker invoker) {
        this.next = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;
        URL url = invocation.getProviderURL();
        Exchanger exchanger = invocation.getExchanger();
        try {
            exchanger.send(invocation.getSerializedRequest());
        } catch (Exception e) {
            throw new InvokerException(e);
        }
        if (next != null) {
            return next.invoke(invocation);
        }
        return null;
    }
}
