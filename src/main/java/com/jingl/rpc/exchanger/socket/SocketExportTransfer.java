//package com.jingl.transfer.socket;
//
//import URL;
//import SendDataFailedException;
//import ServiceExportFailedException;
//import SocketCloseFailedException;
//import Handler;
//import ExportExchanger;
//import Exchanger;
//import org.apache.log4j.Logger;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//
///**
// * Created by Ben on 12/02/2018.
// */
//public class SocketExportTransfer extends Thread implements ExportExchanger {
//    private static Logger logger = Logger.getLogger(SocketExportTransfer.class);
//
//    private ServerSocket serverSocket;
//    private Handler handler;
//    private volatile int PORT;
//    private volatile boolean close = false;
//    private volatile boolean exportSuccess = true;
//
//    public SocketExportTransfer() throws ServiceExportFailedException {
//    }
//
//    public SocketExportTransfer(int port, Handler handler) throws ServiceExportFailedException {
//        setParams(port, handler);
//    }
//
//    @Override
//    public void setParams(int port, Handler handler) throws ServiceExportFailedException {
//        try {
//            this.PORT = port;
//            this.handler = handler;
//            serverSocket = new ServerSocket(PORT);
//        } catch (Exception e) {
//            throw new ServiceExportFailedException(e);
//        }
//    }
//
//    @Override
//    public byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException {
//        return new byte[0];
//    }
//
//    @Override
//    public int export() {
//        start();
//        return 0;
//    }
//
//    @Override
//    public int close() throws SocketCloseFailedException {
//        close = true;
//        if (serverSocket != null && !serverSocket.isClosed()) {
//            try {
//                serverSocket.close();
//            } catch (Exception e) {
//                throw new SocketCloseFailedException(e);
//            }
//
//            //阻塞直到关闭
//            while (!serverSocket.isClosed());
//        }
//        logger.info("服务关闭");
//        return 0;
//    }
//
//    @Override
//    public void run() {
//        //暴露服务，进行监听
//        logger.info("start bind port :" + PORT);
//        while (!close) {
//            try {
//                // 一旦有堵塞, 则表示服务器与客户端获得了连接
//                Socket socket = serverSocket.accept();
//                logger.info("receive from " + socket.getInetAddress().getHostAddress());
//
//                // 处理请求
//                if (handler != null) {
//                    handler.invoke(socket);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
