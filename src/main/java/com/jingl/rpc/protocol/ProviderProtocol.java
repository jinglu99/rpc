package com.jingl.rpc.protocol;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.FailToGenerateInvokerException;
import com.jingl.rpc.utils.PropertyUtils;

/**
 * Created by Ben on 2018/5/19.
 */
public class ProviderProtocol implements Protocol {
    private final ProtocolTemplate protocol = (ProtocolTemplate) ExtensionLoader.getExtensionLoader(Protocol.class, PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_PROTOCOL)).getActiveInstance();

    private volatile Invoker invoker = null;


    @Override
    public Invoker getInvoker() throws FailToGenerateInvokerException {
        if (invoker == null)
            synchronized (ProviderProtocol.class) {
                if (invoker == null) {
                    invoker = protocol.getProviderInvoker();
                }
            }
        return invoker;
    }
}
