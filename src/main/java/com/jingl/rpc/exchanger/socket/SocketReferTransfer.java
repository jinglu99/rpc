//package com.jingl.transfer.socket;
//
//import URL;
//import Request;
//import Response;
//import ConnectionFailedException;
//import SendDataFailedException;
//import SocketCloseFailedException;
//import ReferTransfer;
//import Exchanger;
//import org.apache.commons.lang.ArrayUtils;
//
//import java.io.*;
//import java.net.Socket;
//
///**
// * Created by Ben on 12/02/2018.
// */
//public class SocketReferTransfer implements ReferTransfer {
//
//    private Socket socket;
//    private Request request;
//    private Response response;
//    private volatile URL url;
//
//    public SocketReferTransfer() {}
//
//    public SocketReferTransfer(URL url) throws ConnectionFailedException {
//        setParams(url);
//    }
//
//    @Override
//    public void setParams(URL url) throws ConnectionFailedException {
//        this.url = url;
//        createSocket();
//    }
//
//    @Override
//    public byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException {
//        try {
//            //读取服务器端数据
//            DataInputStream input = new DataInputStream(socket.getInputStream());
//            //向服务器端发送数据
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//            out.write(data);
//
//            InputStream inputStream = socket.getInputStream();
//            OutputStream outputStream = socket.getOutputStream();
//
//            //读取结果
//            byte[] output = new byte[0];
//            int length = 0;
//
//            byte[] tmp = new byte[1024];
//            int tmpLength = inputStream.read(tmp);
//            if (tmpLength != -1) {
//                length += tmpLength;
//                output = ArrayUtils.addAll(output, tmp);
//            }
//            output = ArrayUtils.subarray(output, 0, length);
//
//            out.close();
//            input.close();
//            return output;
//        } catch (Exception e) {
//            System.out.println("客户端异常:" + e.getMessage());
//            throw new SendDataFailedException(e);
//        } finally {
//           close();
//        }
//    }
//
//    @Override
//    public int export() {
//        return 0;
//    }
//
//    /**
//     * 关闭连接
//     * @return
//     * @throws SocketCloseFailedException
//     */
//    @Override
//    public int close() throws SocketCloseFailedException {
//        if (socket != null && !socket.isClosed()) {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                socket = null;
//                System.out.println("客户端 finally 异常:" + e.getMessage());
//                throw new SocketCloseFailedException(e);
//            }
//        }
//        return 0;
//    }
//
//    /**
//     * 建立socket连接
//     * @return
//     * @throws ConnectionFailedException
//     */
//    private Socket createSocket() throws ConnectionFailedException {
//        try {
//            socket = new Socket(url.getIp(), url.getPort());
//            return socket;
//        } catch (IOException e) {
//            throw new ConnectionFailedException(e);
//        }
//    }
//}
