package com.jingl.server;

import com.jingl.entity.LCRPCRequest;
import com.jingl.entity.Request;
import com.jingl.handle.RequestHandle;
import com.jingl.proxy.ProxyFactory;
import com.jingl.proxy.RemoteInterface;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ben on 26/11/2017.
 */
public class Consumer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        RemoteInterface instance = ProxyFactory.getInstance(RemoteInterface.class);

        for (int i = 0; i<1;i++) {
            int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(instance.func("1", 1, 1.0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }
}
