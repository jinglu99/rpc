package com.jingl.transfer.netty;

import com.jingl.common.entity.Request;
import com.jingl.common.entity.Response;
import com.jingl.common.entity.URL;
import com.jingl.common.exceptions.*;
import com.jingl.common.extension.ExtensionLoader;
import com.jingl.handle.Invoker;
import com.jingl.serializer.RPCSerializer;
import com.jingl.transfer.ReferTransfer;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ben on 25/03/2018.
 */
public class NettyReferTransfer extends Thread implements ReferTransfer {
    private static Logger logger = Logger.getLogger(NettyReferTransfer.class);

    private static ConcurrentHashMap<String, BlockingQueue<byte[]>> responses = new ConcurrentHashMap<>();

    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoder(RPCSerializer.class).getActiveInstance();

    private final NettyClient client = new NettyClient();


    @Override
    public byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException {
        try {
            client.send(data);
            return null;
        } catch (Exception e) {
            throw new SendDataFailedException(e);
        }
    }

    @Override
    public int refer() {
        client.connect();
        return 0;
    }

    @Override
    public int close() throws SocketCloseFailedException {
        client.close();
        return 0;
    }


    @Override
    public void setParams(URL url, Invoker invoker) throws ConnectionFailedException {
        client.setParams(url, invoker);
    }
}
