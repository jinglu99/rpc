package com.jingl.rpc.pools;


import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.Transfer;
import com.jingl.rpc.utils.PropertyUtils;
import com.jingl.rpc.transfer.ReferTransfer;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Ben on 01/04/2018.
 */
public class TransferPool {
    private static final Logger logger = Logger.getLogger(TransferPool.class);

    private static final ConcurrentHashMap<URL, CopyOnWriteArrayList<ReferTransfer>> pool = new ConcurrentHashMap<>();

    private static volatile Invoker invoker;

    public static void setInvoker(Invoker invoker) {
        if (TransferPool.invoker == null) {
            synchronized (TransferPool.class) {
                if (TransferPool.invoker == null) {
                    TransferPool.invoker = invoker;
                }
            }
        }
    }

    public static void connect(URL url, Invoker invoker) throws ConnectionFailedException {
        int connections = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_CONSUMER_CONNECTIONS));

        CountDownLatch latch = new CountDownLatch(connections);

        CopyOnWriteArrayList<ReferTransfer> set = new CopyOnWriteArrayList<>();

        for (int i = 0; i < connections; i++) {
            ReferTransfer transfer = (ReferTransfer) ExtensionLoader.getExtensionLoader(ReferTransfer.class).newInstance();
            transfer.setParams(url, invoker);
            set.add(transfer);
        }

        set.parallelStream().forEach( x -> {
            x.refer();
            latch.countDown();
        });

        try {
            latch.await();
            pool.putIfAbsent(url, set);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Transfer getTransfer(URL url) throws ConnectionFailedException {
        CopyOnWriteArrayList list = pool.get(url);

        if (list==null || list.size() == 0) {
            connect(url, invoker);
            list = pool.get(url);
        }

        if (list != null && list.size()>0) {
            int index = (int) (Math.random() * list.size());
            return (Transfer) list.get(index);
        }
        return null;
    }


}
