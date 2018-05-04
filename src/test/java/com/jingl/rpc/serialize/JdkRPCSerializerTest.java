package com.jingl.rpc.serialize;

import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.exceptions.SerializeException;
import com.jingl.rpc.serializer.JdkRPCSerializer;
import com.jingl.rpc.serializer.RPCSerializer;
import org.junit.Test;

/**
 * Created by Ben on 13/02/2018.
 */
public class JdkRPCSerializerTest {
    @org.junit.Test
    public void serialize() throws SerializeException {
        Request request = new Request();
        request.setInterfaceName("test");
        request.setMethodName("helloworld");
        request.setParams(new Object[]{"hello",1});
        request.setTypes(new Class[]{String.class, int.class});

        RPCSerializer serializer = new JdkRPCSerializer();

        System.out.println(new String(serializer.serialize(request)));
    }



    @Test
    public void deserialize() throws SerializeException {
        Request request = new Request();
        request.setInterfaceName("test");
        request.setMethodName("helloworld");
        request.setParams(new Object[]{"hello",1});
        request.setTypes(new Class[]{String.class, int.class});

        RPCSerializer serializer = new JdkRPCSerializer();

        Request request1 = serializer.deserialize(serializer.serialize(request), Request.class);
        System.out.println(request1.toString());
    }
}
