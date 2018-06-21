package com.jingl.rpc.protocol;

import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.common.exceptions.FailToGenerateInvokerException;
import org.junit.Test;

/**
 * Created by Ben on 2018/5/19.
 */
public class ProtocolTest {
    @Test
    public void consumerProtocol() throws FailToGenerateInvokerException {
        Protocol protocol = (Protocol) ExtensionLoader.getExtensionLoader(Protocol.class,"consumer").getActiveInstance();
        protocol.getInvoker();
        System.out.println("ok");
    }

    @Test
    public void providerProtocol() throws FailToGenerateInvokerException {
        Protocol protocol = (Protocol) ExtensionLoader.getExtensionLoader(Protocol.class,"provider").getActiveInstance();
        protocol.getInvoker();
        System.out.println("ok");
    }

    @Test
    public void myProtocol() throws FailToGenerateInvokerException {
        ProtocolTemplate protocol = (ProtocolTemplate) ExtensionLoader.getExtensionLoader(Protocol.class,"my").getActiveInstance();
        protocol.getConsumerInvoker();
        System.out.println("ok");
    }
}
