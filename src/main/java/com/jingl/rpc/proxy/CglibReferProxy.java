package com.jingl.rpc.proxy;

import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.FailToGenerateInvokerException;
import com.jingl.rpc.protocol.Protocol;
import net.sf.cglib.proxy.Enhancer;

/**
 * Created by Ben on 27/11/2017.
 */
public class CglibReferProxy implements Proxy{

    public CglibReferProxy() {
    }

    public <T> T getInstance(Class T) throws FailToGenerateInvokerException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(T);
        enhancer.setCallback(new RPCProxy());

        return (T) enhancer.create();
    }
}
