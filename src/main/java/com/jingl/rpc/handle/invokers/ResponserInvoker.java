package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.common.exceptions.SerializeException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.InvocationPool;
import com.jingl.rpc.serializer.RPCSerializer;

/**
 * 异步返回调用结果
 * Created by Ben on 2018/4/20.
 */
public class ResponserInvoker implements Invoker {
    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class).getActiveInstance();

    @Override
    public Object invoke(Object val) throws InvokerException {
        byte[] bytes = (byte[]) val;
        Long id = null;
        Response response = null;
        Invocation invocation = null;
        try {
            response = serializer.deserialize(bytes, Response.class);
            id = response.getId();
            invocation = InvocationPool.getInvocation(id);
            invocation.getQueue().add(response);
        } catch (Exception e) {
            invocation = InvocationPool.getInvocation(id);
            e.printStackTrace();
        }
        return null;
    }
}
