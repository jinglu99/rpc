package com.jingl.handle;

import com.alibaba.fastjson.JSON;
import com.jingl.entity.LCRPCRequest;
import com.jingl.entity.Request;
import com.jingl.entity.Response;
import com.jingl.proxy.ProviderProxy;

import java.io.*;
import java.net.Socket;

/**
 * Created by Ben on 26/11/2017.
 */
public class ProviderHandle implements Runnable {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private LCRPCRequest request;

    public ProviderHandle(Socket socket) {
        this.socket = socket;
    }

    private LCRPCRequest getRequest() throws Exception{
        // 读取客户端数据
        input = new DataInputStream(socket.getInputStream());
        String data = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
        request = JSON.parseObject(data, LCRPCRequest.class);
        return request;
    }

    private void sendResponse(String response) throws Exception {
        // 向客户端回复信息
        output = new DataOutputStream(socket.getOutputStream());
        output.writeUTF(response);
        input.close();
        output.close();
    }

    @Override
    public void run() {
        try {
            getRequest();
            ProviderProxy proxy = new ProviderProxy();
            Response response = proxy.procced(request);
            String rsp = JSON.toJSONString(response);
            sendResponse(rsp);
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
}
