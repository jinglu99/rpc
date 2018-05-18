package com.jingl.rpc.pools;


import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.DeadProviderException;
import com.jingl.rpc.common.exceptions.NoAvailableConnectionException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.Transfer;
import com.jingl.rpc.utils.PropertyUtils;
import com.jingl.rpc.transfer.ReferTransfer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

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

    public static void connect(URL url, Invoker invoker) {
        synchronized (url) {
            CopyOnWriteArrayList<ReferTransfer> set = null;
            if (pool.containsKey(url)) {
                set = pool.get(url);
            } else {
                set = new CopyOnWriteArrayList<>();
                pool.putIfAbsent(url, set);
            }

            int connections = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_CONSUMER_CONNECTIONS));

            CountDownLatch latch = new CountDownLatch(connections);

            List<ReferTransfer> tmp = new ArrayList();
            for (int i = 0; i < connections; i++) {
                ReferTransfer transfer = (ReferTransfer) ExtensionLoader.getExtensionLoader(ReferTransfer.class).newInstance();
                transfer.setParams(url, invoker);
                tmp.add(transfer);

            }

            CopyOnWriteArrayList<ReferTransfer> finalSet = set;
            tmp.parallelStream().forEach(x->{
                try {
                    x.refer();
                    finalSet.add(x);
                } catch (ConnectionFailedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public static Transfer getTransfer(URL url) throws NoAvailableConnectionException, DeadProviderException {
        CopyOnWriteArrayList<ReferTransfer> list = pool.get(url);
        if (list == null || list.size() == 0) {   //建立连接
            synchronized (url) {
                if (!pool.containsKey(url)) {
                    connect(url, invoker);
                }
            }
            list = pool.get(url);
        }

        if (list == null)
            throw new DeadProviderException();

        List candidate = list.parallelStream().filter(x -> x.isActive()).collect(Collectors.toList());

        if (candidate == null || candidate.size() == 0) {
            for (ReferTransfer transfer : list) {
                if (!transfer.isDead())
                    throw new NoAvailableConnectionException();
            }
            throw new DeadProviderException();
        }


        //随机返回一个连接
        int index = (int) (Math.random() * candidate.size());
        return (Transfer) candidate.get(index);
    }

    public static void remove(URL url) {
        CopyOnWriteArrayList<ReferTransfer> list = pool.get(url);
        if (list != null && list.size() > 0) {
            list.parallelStream().forEach(x -> {
                if (x.isActive())
                    try {
                        x.close();
                    } catch (SocketCloseFailedException e) {
                        e.printStackTrace();
                    }
            });
        }
    }


}
