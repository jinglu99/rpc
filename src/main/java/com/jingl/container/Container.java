package com.jingl.container;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/12/2017.
 */
public class Container {
    private static ConcurrentHashMap<Class, Object> container = new ConcurrentHashMap();

    public Object getInstance(Class clazz) {
        return container.get(clazz);
    }

    public void setInstance(Class clazz, Object obj) {
        container.put(clazz, obj);
    }
}
