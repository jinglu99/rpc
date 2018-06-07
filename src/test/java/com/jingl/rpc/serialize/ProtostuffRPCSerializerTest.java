package com.jingl.rpc.serialize;

import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.exceptions.SerializeException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.serializer.RPCSerializer;
import org.jboss.netty.util.internal.ReusableIterator;
import org.junit.Test;

/**
 * Created by Ben on 2018/5/10.
 */
public class ProtostuffRPCSerializerTest {
    @Test
    public void test() throws SerializeException {
        Request request = new Request();
        request.setId(System.currentTimeMillis());

        RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class, "protostuff").getActiveInstance();
        byte[] bytes = serializer.serialize(request);
        Request tmp = serializer.deserialize(bytes, Request.class);
        System.out.println(tmp.toString());
    }
}
