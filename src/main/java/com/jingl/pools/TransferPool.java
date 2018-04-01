package com.jingl.pools;


import com.jingl.common.Constants;
import com.jingl.common.entity.URL;
import com.jingl.common.exceptions.ConnectionFailedException;
import com.jingl.common.extension.ExtensionLoader;
import com.jingl.handle.Invoker;
import com.jingl.handle.invokers.ExchangeInvoker;
import com.jingl.transfer.ReferTransfer;
import com.jingl.transfer.Transfer;
import com.jingl.transfer.netty.NettyReferTransfer;
import com.jingl.utils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Ben on 01/04/2018.
 */
public class TransferPool {
    private static final Logger logger = Logger.getLogger(TransferPool.class);

    private static final ConcurrentHashMap<URL, CopyOnWriteArrayList<ReferTransfer>> pool = new ConcurrentHashMap<>();

    public static void connect(URL url, Invoker invoker) throws ConnectionFailedException {
        int connections = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_CONSUMER_CONNECTIONS));

        CountDownLatch latch = new CountDownLatch(connections);

        CopyOnWriteArrayList<ReferTransfer> set = new CopyOnWriteArrayList<>();

        for (int i = 0; i < connections; i++) {
            ReferTransfer transfer = (ReferTransfer) ExtensionLoader.getExtensionLoder(ReferTransfer.class).newInstance();
            transfer.setParams(url, invoker);
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

    public static Transfer getTransfer(URL url) {
        CopyOnWriteArrayList list = pool.get(url);
        if (list != null && list.size()>0) {
            int index = (int) (Math.random() * list.size());
            return (Transfer) list.get(index);
        }
        return null;
    }


}
