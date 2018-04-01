package com.jingl.netty.test;

import com.jingl.common.entity.Request;
import com.jingl.common.entity.Response;
import com.jingl.common.entity.URL;
import com.jingl.common.exceptions.InvokerException;
import com.jingl.common.exceptions.SerializeException;
import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.common.exceptions.SocketCloseFailedException;
import com.jingl.handle.Invoker;
import com.jingl.serializer.FastjsonRPCSerializer;
import com.jingl.serializer.RPCSerializer;
import com.jingl.transfer.netty.NettyExportTransfer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Ben on 31/03/2018.
 */
public class ExportTest {

    @Test
    public void nettyExportTest() throws ServiceExportFailedException, InterruptedException, IOException, SocketCloseFailedException {
        NettyExportTransfer transfer = new NettyExportTransfer();
        URL url = new URL(null, "localhost", 2532, null, null);
        transfer.setParams(0, new TestInvoker());
        transfer.export();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("input quit to exit");
        while (true) {
            String input = in.readLine();
            if (input.equals("quit")) {
                transfer.close();
                return;
            }
        }
    }


    private class TestInvoker implements Invoker {

        @Override
        public Object invoke(Object val) throws InvokerException {
            byte[] data = (byte[]) val;
            Request request;
            RPCSerializer serializer = new FastjsonRPCSerializer();
            try {
                request = serializer.deserialize(data, Request.class);
            } catch (SerializeException e) {
                e.printStackTrace();
                return null;
            }
            System.out.println("received: " + request.getMethodName());
            Response response = new Response(request.getId(), "hello world");
            try {
                byte[] rep = serializer.serialize(response);
                return rep;
            } catch (SerializeException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
