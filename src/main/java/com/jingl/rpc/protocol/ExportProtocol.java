package com.jingl.rpc.protocol;

import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.PackageInvoker;
import com.jingl.rpc.handle.invokers.ExceptionInvoker;
import com.jingl.rpc.handle.invokers.MethodInvoker;

/**
 * Created by Ben on 2018/4/23.
 */
public class ExportProtocol implements Protocol {
    @Override
    public Invoker getInvoker() {

        Invoker methodInvoker = new MethodInvoker();

        Invoker exceptionInvoker = new ExceptionInvoker(methodInvoker);

        Invoker deserializeInvoker = new PackageInvoker(exceptionInvoker);

        return deserializeInvoker;
    }
}
