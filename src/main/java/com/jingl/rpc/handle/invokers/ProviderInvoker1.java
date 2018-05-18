package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.container.Container;
import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.serializer.FastjsonRPCSerializer;
import com.jingl.rpc.serializer.RPCSerializer;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created by Ben on 13/02/2018.
 */
public class ProviderInvoker1 implements Invoker,Runnable {

    Container container = new Container();
    RPCSerializer serializer = new FastjsonRPCSerializer();

    private Socket socket;
    private Request request;
    private InputStream inputStream;
    private OutputStream outputStream;


    private Request getRequest() throws Exception{
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();


        // 读取客户端数据
        byte[] input = new byte[0];
        int length = 0;

        byte[] tmp = new byte[1024];
        int data = inputStream.read(tmp);
        if (data != -1) {
            length += data;
            input = ArrayUtils.addAll(input, tmp);
        }
        input = ArrayUtils.subarray(input, 0, length);
        request = serializer.deserialize(input, Request.class);
        return request;
    }

    private void sendResponse(Response response) throws Exception {
        // 向客户端回复信息
        byte[] data  = serializer.serialize(response);
        outputStream.write(data);
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void run() {
        try {
            getRequest();
            Response response = invoke();
            sendResponse(response);
        }   catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        this.socket = (Socket) val;
        new Thread(this).start();
        return null;
    }

    public Response invoke() throws InvokerException {
        try {
            Class clazz = Class.forName(request.getInterfaceName());
            Object instance = container.getInstance(clazz);
            Method method = clazz.getMethod(request.getMethodName(), request.getTypes());
            Object result = method.invoke(instance, request.getParams());
//            return new Response(result);
            return null;
        } catch (Exception e) {
            throw new InvokerException(e);
        }
    }
}
