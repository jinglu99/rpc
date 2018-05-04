package com.jingl.rpc.server;

import com.jingl.rpc.proxy.CglibReferProxy;
import com.jingl.rpc.proxy.Proxy;
import com.jingl.rpc.proxy.RemoteInterface;

import java.beans.SimpleBeanInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ben on 26/11/2017.
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        int wait = 0;
        int requestNo = 1;
        int threadNo = 1;
        for (int i = 0; i<wait; i++) {
            Thread.sleep(1000);
            System.out.println(wait-i);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threadNo);
        Proxy proxy = new CglibReferProxy();
        RemoteInterface instance = proxy.getInstance(RemoteInterface.class);
        CountDownLatch latch = new CountDownLatch(requestNo);


        long startTime = System.currentTimeMillis();
        for (int i = 0; i<requestNo;i++) {
            int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        instance.func("1", 1, 1.0);
//                        System.out.println(instance.func("1", 1, 1.0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");

        System.out.println("send " + requestNo + " requests");
        Date start = new Date(startTime);
        Date end = new Date(endTime);

        System.out.println("start time: " + sdf.format(start));
        System.out.println("end time: " + sdf.format(end));
        System.out.println("cost: " + (endTime - startTime) + " ms");
        System.out.println("qps: " + ((double)(requestNo/(endTime - startTime)* 1000))  + " req/s");

    }
}
