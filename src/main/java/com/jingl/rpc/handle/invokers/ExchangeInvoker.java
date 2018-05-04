package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.TransferPool;
import com.jingl.rpc.transfer.Transfer;

/**
 * Created by Ben on 01/04/2018.
 */
public class ExchangeInvoker implements Invoker {
    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;
        URL url = invocation.getProviderURL();
        Transfer transfer = invocation.getTransfer();
        try {
            transfer.send(invocation.getSerializedRequest());
        } catch (Exception e) {
            throw new InvokerException(e);
        }
        return null;
    }
}
