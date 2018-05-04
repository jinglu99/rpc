package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.common.exceptions.SerializeException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.serializer.RPCSerializer;
import org.apache.log4j.Logger;

/**
 * 序列化操作
 * Created by Ben on 01/04/2018.
 */
public class SerializeInvoker implements Invoker {
    private static final Logger logger = Logger.getLogger(SerializeInvoker.class);

    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class).getActiveInstance();

    private volatile Invoker nextInvoker;

    public SerializeInvoker(Invoker invoker) {
        this.nextInvoker = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;

        if (invocation.isInterupt())
            return null;

        try {
            byte[] msg = serializer.serialize(invocation.getRequest());

            if (invocation.getSerializedRequest() == null)
                invocation.setSerializedRequest(msg);

            return nextInvoker.invoke(invocation);
        } catch (SerializeException e) {
            logger.error("cant't seialize message:" + val.toString());
            throw new InvokerException(e);
        }
    }
}
