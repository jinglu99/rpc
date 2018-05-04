package com.jingl.rpc.proxy;

import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.protocol.Protocol;
import net.sf.cglib.proxy.Enhancer;

/**
 * Created by Ben on 27/11/2017.
 */
public class CglibReferProxy implements Proxy{
    public <T> T getInstance(Class T) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(T);

        Protocol protocol = (Protocol) ExtensionLoader.getExtensionLoader(Protocol.class).getActiveInstance();

        Invoker invoker = protocol.getInvoker();
        enhancer.setCallback(new RPCProxy(invoker));

        return (T) enhancer.create();
    }
}
