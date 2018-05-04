package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.Transfer;
import org.apache.log4j.Logger;

/**
 * 路由逻辑
 * Created by Ben on 2018/5/2.
 */
public class RouterInvoker implements Invoker {
    private final Logger logger = Logger.getLogger(RouterInvoker.class);

    private final Cluster cluster = (Cluster) ExtensionLoader.getExtensionLoader(Cluster.class).getActiveInstance();
    private final Invoker nextInvoker;

    public RouterInvoker(Invoker nextInvoker) {
        this.nextInvoker = nextInvoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;
        if (invocation.isInterupt()) {
            return null;
        }

        try {
            Transfer transfer = cluster.getTransfer(invocation.getClazz());
            invocation.setTransfer(transfer);
            return nextInvoker.invoke(invocation);
        } catch (InvokerException e) {
            throw e;
        } catch (Exception e) {
            throw new InvokerException(e);
        }
    }
}
