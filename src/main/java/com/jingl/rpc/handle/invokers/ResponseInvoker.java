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
public class ResponseInvoker implements Invoker {
    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class).getActiveInstance();

    private final Invoker before;
    private final Invoker next;

    public ResponseInvoker(Invoker before, Invoker next) {
        this.before = before;
        this.next = next;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        byte[] bytes = (byte[]) val;
        Long id = null;
        Response response = null;
        Invocation invocation = null;
        try {
            response = serializer.deserialize(bytes, Response.class);
            id = response.getId();

            //返回结果前调用
            if (before != null) {
                before.invoke(response);
            }

            invocation = InvocationPool.getInvocation(id);
            invocation.getQueue().add(response);

            //返回结果后调用
            if (next != null) {
                next.invoke(response);
            }
        } catch (Exception e) {
            invocation = InvocationPool.getInvocation(id);
            e.printStackTrace();
        }
        return null;
    }
}
