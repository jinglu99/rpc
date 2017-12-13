package com.jingl.service;

import com.google.gson.Gson;
import com.jingl.common.Constant;
import com.jingl.common.GetRemoteInfo;
import com.jingl.entity.LCRPCRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 26/11/2017.
 */
public class ConsumerServiceImpl {

    public Set<String> getServiceIPsByID(String serviceID) {

        //调用服务注册查找中心的服务,获取ip列表
        Set<String> ips = new HashSet<>();
        String url = "http://" + Constant.SERVICEACCESSCENTER_IP + ":" + Constant.SERVICEACCESSCENTER_PORT + "/"
                + Constant.QUERYSERVICEIPSBYID + "?serviceID=" + serviceID;

        Set status = new HashSet<Integer>();
        status.add(200);
        StringBuilder response = new StringBuilder();
        GetRemoteInfo.getRemoteInfo(url, "GET", null, null, response, status);
        if (response.length() == 0) return ips;
        ips = new Gson().fromJson(response.toString(), ips.getClass());
        return ips;
    }

    public String getIP(String serviceID, String methodName, List<Object> params, Set<String> ips) {
        //可以根据接口\方法\参数进行路由,这里我们先简单实现,选出列表的第一个,模拟路由的过程
        String[] temparr = new String[ips.size()];
        ips.toArray(temparr);
        return temparr[0];
    }

    public LCRPCRequest getRequestDO(String interfaceName, String version, String methodName, List<Object> params) {
        LCRPCRequest requestDO = new LCRPCRequest();
        requestDO.setInterfaceName(interfaceName);
        requestDO.setMethodName(methodName);
        requestDO.setParams(params.toArray());
        requestDO.setVersion(version);
        return requestDO;
    }

    public Object sendData(String ip, LCRPCRequest requestDO) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutputStream = null;
        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        try {
            socket = new Socket(ip, Constant.PORT);//向远程服务端建立连接
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());//获得输出流
            objectOutputStream.writeObject(requestDO);//发送序列化结果
            objectOutputStream.flush();
            socket.shutdownOutput();
            //等待响应
            objectInputStream = new ObjectInputStream(socket.getInputStream());//获得输入流
            Object result = objectInputStream.readObject();//序列化为Object对象
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (objectInputStream != null) objectInputStream.close();
                if (objectOutputStream != null) objectOutputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

        }

    }
}
