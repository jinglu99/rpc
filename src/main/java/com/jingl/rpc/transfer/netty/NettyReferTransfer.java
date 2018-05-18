package com.jingl.rpc.transfer.netty;

import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.SendDataFailedException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;
import com.jingl.rpc.serializer.RPCSerializer;
import com.jingl.rpc.transfer.ReferTransfer;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 25/03/2018.
 */
public class NettyReferTransfer extends Thread implements ReferTransfer {
    private static Logger logger = Logger.getLogger(NettyReferTransfer.class);

    private static ConcurrentHashMap<String, BlockingQueue<byte[]>> responses = new ConcurrentHashMap<>();

    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class).getActiveInstance();

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
    public boolean isActive() {
        return client.isActive();
    }

    @Override
    public int refer() throws ConnectionFailedException {
        client.connect();
        return 0;
    }

    @Override
    public boolean isDead() {
        return client.isDead();
    }

    @Override
    public int close() throws SocketCloseFailedException {
        client.close();
        return 0;
    }


    @Override
    public void setParams(URL url, Invoker invoker) {
        client.setParams(url, invoker);
    }
}
