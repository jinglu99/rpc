package com.jingl.transfer;

import com.jingl.common.exceptions.SendDataFailedException;
import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.common.exceptions.SocketCloseFailedException;
import com.jingl.handle.Handler;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ben on 12/02/2018.
 */
public class SocketExportTransfer extends Thread implements Transfer {

    private ServerSocket serverSocket;
    private Handler handler;
    private final int PORT;
    private volatile boolean close = false;
    private volatile boolean exportSuccess = true;

    public SocketExportTransfer(int port, Handler handler) throws ServiceExportFailedException {
        try {
            this.PORT = port;
            this.handler = handler;
            serverSocket = new ServerSocket(PORT);
        } catch (Exception e) {
            throw new ServiceExportFailedException(e);
        }

    }

    @Override
    public byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException {
        return new byte[0];
    }

    @Override
    public int export() {
        start();
        return 0;
    }

    @Override
    public int close() throws SocketCloseFailedException {
        close = true;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                throw new SocketCloseFailedException(e);
            }

            //阻塞直到关闭
            while (!serverSocket.isClosed());
        }
        System.out.println("服务关闭");
        return 0;
    }

    @Override
    public void run() {
        //暴露服务，进行监听
        System.out.println("start bind port :" + PORT);
        while (!close) {
            try {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket socket = serverSocket.accept();
                System.out.println("receive from " + socket.getInetAddress().getHostAddress());

                // 处理请求
                if (handler != null) {
                    handler.invoke(socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
