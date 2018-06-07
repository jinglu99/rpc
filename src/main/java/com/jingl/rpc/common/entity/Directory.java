package com.jingl.rpc.common.entity;

import com.jingl.rpc.cluster.LoadBalance;
import com.jingl.rpc.common.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Ben on 2018/4/20.
 */
public class Directory {
    private final Class clazz;

    private final CopyOnWriteArraySet<URL> urls = new CopyOnWriteArraySet();

    private static LoadBalance loadBalance = (LoadBalance) ExtensionLoader.getExtensionLoader(LoadBalance.class).getActiveInstance();

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

    public URL getOne(Invocation invocation) {
        return (URL) loadBalance.select(invocation, urls);
    }
}
