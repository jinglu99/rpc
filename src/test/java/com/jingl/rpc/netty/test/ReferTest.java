package com.jingl.rpc.netty.test;

import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.*;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.Transfer;
import com.jingl.rpc.serializer.FastjsonRPCSerializer;
import com.jingl.rpc.serializer.RPCSerializer;
import com.jingl.rpc.transfer.netty.NettyReferTransfer;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 31/03/2018.
 */
public class ReferTest {

    private final ConcurrentHashMap<Long, BlockingQueue> responses = new ConcurrentHashMap();


    @Test
    public void referTest() throws ConnectionFailedException, SerializeException, SocketCloseFailedException, InterruptedException, SendDataFailedException {
        NettyReferTransfer transfer = new NettyReferTransfer();
        URL url = new URL(null, "localhost", 2532, null, null);
        ReferInvoker invoker = new ReferInvoker();
        transfer.setParams(url, invoker);
        transfer.refer();

        Response response = send(transfer, "hello");

        System.out.println(response.getResponse().toString());
    }

    private Response send(Transfer transfer, String msg) throws SerializeException, SocketCloseFailedException, SendDataFailedException, InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue(1);
        Request request = new Request();

        request.setId(System.currentTimeMillis());
        request.setMethodName(msg);

        responses.put(request.getId(), queue);

        RPCSerializer serializer = new FastjsonRPCSerializer();
        byte[] req = serializer.serialize(request);
        transfer.send(req);

        Response response = (Response) queue.take();
        return response;
    }

    private class ReferInvoker implements Invoker {

        @Override
        public Object invoke(Object val) throws InvokerException {
            RPCSerializer serializer = new FastjsonRPCSerializer();

            byte[] rep = (byte[]) val;
            try {
                Response response = serializer.deserialize(rep, Response.class);
                BlockingQueue queue = responses.get(response.getId());
                queue.add(response);

            } catch (SerializeException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
