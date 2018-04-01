package com.jingl.handle.invokers;

import com.jingl.common.entity.Invocation;
import com.jingl.common.entity.Request;
import com.jingl.common.exceptions.InvokerException;
import com.jingl.common.exceptions.SerializeException;
import com.jingl.common.extension.ExtensionLoader;
import com.jingl.handle.Invoker;
import com.jingl.serializer.RPCSerializer;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * Created by Ben on 01/04/2018.
 */
public class SerializeInvoker implements Invoker {
    private static final Logger logger = Logger.getLogger(SerializeInvoker.class);

    private volatile Invoker nextInvoker;

    public SerializeInvoker(Invoker invoker) {
        this.nextInvoker = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;
        RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoder(RPCSerializer.class).getActiveInstance();

        try {
            byte[] msg = serializer.serialize(invocation.getResponse());

            if (invocation.getSerializedRequest() != null)
                invocation.setSerializedRequest(msg);

            return nextInvoker.invoke(msg);
        } catch (SerializeException e) {
            logger.error("cant't seialize message:" + val.toString());
            throw new InvokerException(e);
        }
    }
}
