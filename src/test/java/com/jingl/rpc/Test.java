package com.jingl.rpc;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Ben on 26/11/2017.
 */
public class Test {
    private static final String CLIENT_PORT = "2121";

    protected static final String hosts = "localhost:2181";



    /**
     * 连接的超时时间, 毫秒
     */
    private final int SESSION_TIMEOUT = 5000;
    private static CountDownLatch connectedSignal = new CountDownLatch(1);
    CuratorFramework client;

    public void connect() {
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2121", new RetryNTimes(10, 5000));
    }

//    private void addWatcher() {
//        client.getChildren()
//    }

    public static void main(String[] args) throws Exception {
//
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new RetryNTimes(10, 5000));
        client.start();// 连接
        // 获取子节点，顺便监控子节点
        List<String> children = client.getChildren().usingWatcher(new CuratorWatcher()
        {
            @Override
            public void process(WatchedEvent event) throws Exception
            {
                System.out.println("监控： " + event);
                List<String> children = client.getChildren().usingWatcher(new CuratorWatcher()
                {
                    @Override
                    public void process(WatchedEvent event) throws Exception
                    {
                        System.out.println("监控： " + event);

                    }
                }).forPath("/");
                System.out.println(children);
            }
        }).forPath("/");
        System.out.println(children);
        String result = client.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/t/data");

        client.delete().forPath("/t/data");
        connectedSignal.await();
    }

    class Watcher implements CuratorWatcher {
        @Override
        public void process(WatchedEvent watchedEvent) throws Exception {

        }
    }
}
