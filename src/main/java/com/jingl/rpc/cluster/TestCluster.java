package com.jingl.rpc.cluster;

import com.google.common.collect.Lists;
import com.jingl.rpc.common.entity.Directory;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.NoProviderFoundException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.pools.TransferPool;
import com.jingl.rpc.register.Register;
import com.jingl.rpc.register.zk.ZookeeperRegister;
import com.jingl.rpc.transfer.Transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Ben on 2018/4/20.
 */
public class TestCluster implements Cluster {

    private final ConcurrentHashMap<Class, Directory> map = new ConcurrentHashMap<>();
    private final Register register = (Register) ExtensionLoader.getExtensionLoader(Register.class, "zookeeper").getActiveInstance();

    @Override
    public void init() {
        register.connect();
    }

    public void connect(Invoker invoker) throws ConnectionFailedException {
//        TransferPool.connect(url, invoker);
    }

    @Override
    public Transfer getTransfer(Class clazz) throws NoProviderFoundException {
        Directory directory = map.get(clazz);
        if (directory == null)
            throw new NoProviderFoundException();

        URL url = directory.getOne();
        if (url == null) {
            throw new NoProviderFoundException();
        }


        Transfer transfer = null;
        try {
            transfer = TransferPool.getTransfer(url);
        } catch (ConnectionFailedException e) {
            directory.remove(url);
        }
        return transfer;
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
