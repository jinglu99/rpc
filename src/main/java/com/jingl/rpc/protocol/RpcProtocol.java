package com.jingl.rpc.protocol;

import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.cluster.TestCluster;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.*;
import com.jingl.rpc.pools.TransferPool;

/**
 * Created by Ben on 2018/4/19.
 */
public class RpcProtocol implements Protocol {
    private final Cluster cluster = (Cluster) ExtensionLoader.getExtensionLoader(Cluster.class).getActiveInstance();

    @Override
    public Invoker getInvoker() {
        try {
            Invoker responseInvoker = new ResponserInvoker();

            TransferPool.setInvoker(responseInvoker);
            cluster.init();

            Invoker exchangeInvoker = new ExchangeInvoker();

            Invoker serializeInvoker = new SerializeInvoker(exchangeInvoker);

            Invoker futureInvoker = new FutureInvoker(serializeInvoker);

            Invoker routerInvoker = new RouterInvoker(futureInvoker);

            FailoverInvoker failoverInvoker = new FailoverInvoker(routerInvoker);

            return failoverInvoker;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
