package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.common.exceptions.SerializeException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.serializer.RPCSerializer;

/**服务端反序列化
 * Created by Ben on 2018/4/23.
 */
public class PackageInvoker implements Invoker {

    private final Invoker next;
    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class).getActiveInstance();

    public PackageInvoker(Invoker invoker) {
        next = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        byte[] bytes = (byte[]) val;
        try {
            Request request = serializer.deserialize(bytes, Request.class);
            return serializer.serialize(next.invoke(request));
        } catch (SerializeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
