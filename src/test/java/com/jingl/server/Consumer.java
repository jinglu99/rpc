package com.jingl.server;

import com.jingl.proxy.CglibReferProxy;
import com.jingl.proxy.Proxy;
import com.jingl.proxy.RemoteInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ben on 26/11/2017.
 */
public class Consumer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        Proxy proxy = new CglibReferProxy();
        RemoteInterface instance = proxy.getInstance(RemoteInterface.class);


        for (int i = 0; i<1000000;i++) {
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
