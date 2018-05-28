package com.jingl.rpc.cluster;

import com.jingl.rpc.common.entity.Directory;
import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.DeadProviderException;
import com.jingl.rpc.common.exceptions.NoAvailableConnectionException;
import com.jingl.rpc.common.exceptions.NoProviderFoundException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.exchanger.Exchanger;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.ExchangerPool;
import com.jingl.rpc.register.Register;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 2018/4/20.
 */
public class RpcCluster implements Cluster {

    private final ConcurrentHashMap<Class, Directory> map = new ConcurrentHashMap<>();
    private final Register register = (Register) ExtensionLoader.getExtensionLoader(Register.class, "zookeeper").getActiveInstance();

    @Override
    public void init() {
        register.connect();
    }

    public void connect(Invoker invoker) throws ConnectionFailedException {
    }

    @Override
    public Exchanger getTransfer(Invocation invocation) throws NoProviderFoundException, NoAvailableConnectionException {
        Directory directory = map.get(invocation.getClazz());
        if (directory == null)
            throw new NoProviderFoundException();

        URL url = directory.getOne(invocation);
        if (url == null) {
            throw new NoProviderFoundException();
        }


        Exchanger exchanger = null;
        try {
            exchanger = ExchangerPool.getTransfer(url);
        } catch (DeadProviderException e) {
            synchronized (url) {
                ExchangerPool.remove(url);
                directory.remove(url);
            }
            throw new NoAvailableConnectionException();
        }
        return exchanger;
    }

    @Override
    public Directory getDirectory(Class clazz) {
        Directory directory = map.get(clazz);
        if (directory == null) {
            map.putIfAbsent(clazz, new Directory(clazz));
            directory = map.get(clazz);
        }
        return directory;
    }


}
