package com.jingl.handle;

import com.alibaba.fastjson.JSON;
import com.jingl.entity.LCRPCRequest;
import com.jingl.entity.Request;
import com.jingl.entity.Response;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Ben on 26/11/2017.
 */
public class RequestHandle {
    private Socket socket;
    private LCRPCRequest request;
    private Response response;
    public static final String IP_ADDR = "localhost";//服务器地址
    public static final int PORT = 12345;//服务器端口号

    public RequestHandle(LCRPCRequest request) {
        this.request = request;
    }

    public void createSocket() throws Exception {
        this.createSocket(IP_ADDR, PORT);
    }

    public void createSocket(String IP, int port) throws Exception{
        socket = new Socket(IP, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public LCRPCRequest getRequest() {
        return request;
    }

    public void setRequest(LCRPCRequest request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public RequestHandle(Socket socket) {
        this.socket = socket;
    }

    public Response proceed() throws Exception{
        try {
            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String req = JSON.toJSONString(request);
            out.writeUTF(req);

            String ret = input.readUTF();
            out.close();
            input.close();
            response = JSON.parseObject(ret, Response.class);
            return response;
        } catch (Exception e) {
            System.out.println("客户端异常:" + e.getMessage());
            throw e;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                    System.out.println("客户端 finally 异常:" + e.getMessage());
                }
            }
        }
    }
}
