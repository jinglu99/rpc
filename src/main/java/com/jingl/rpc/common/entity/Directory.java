package com.jingl.rpc.common.entity;

import com.jingl.rpc.pools.TransferPool;
import com.jingl.rpc.transfer.Transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Ben on 2018/4/20.
 */
public class Directory {
    private final Class clazz;

    private final CopyOnWriteArraySet<URL> urls = new CopyOnWriteArraySet();

    public Directory(Class clazz) {
        this.clazz = clazz;
    }

    public void add(URL url) {
        urls.add(url);
    }

    public void remove(URL url) {
        urls.remove(url);
    }

    public List<URL> getURLS() {
        return new ArrayList<URL>(urls);
    }

    public URL getOne() {
        //// TODO: 2018/5/1 软负载均衡逻辑
        List<URL> tmp = new ArrayList<>(urls);
        if (tmp.size() > 0) {
            int index = (int) (Math.random() * tmp.size());
            return tmp.get(index);
        }

        return null;
    }
}
