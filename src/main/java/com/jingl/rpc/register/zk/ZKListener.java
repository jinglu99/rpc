package com.jingl.rpc.register.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * Created by Ben on 2018/4/27.
 */
public class ZKListener implements TreeCacheListener {
    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
        System.out.println("zk changed");
    }
}
