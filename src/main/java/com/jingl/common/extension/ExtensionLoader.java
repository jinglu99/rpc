package com.jingl.common.extension;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/02/2018.
 */
public class ExtensionLoader<T> {
    private final Class<?> type;

    private final static ConcurrentHashMap<Class, ExtensionLoader> EXTENSION_LODERS = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Class, Object> EXTENSION_INSTANCE = new ConcurrentHashMap<>();

    public static ExtensionLoader getExtensionLoder(Class clazz) {
        ExtensionLoader loader = EXTENSION_LODERS.get(clazz);
        if (loader == null) {
            EXTENSION_LODERS.putIfAbsent(clazz, new ExtensionLoader(clazz));
            loader = EXTENSION_LODERS.get(clazz);
        }
        return loader;
    }

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public T newInstance() throws IllegalAccessException, InstantiationException {
        return (T) type.newInstance();
    }

    public T getActiveInstance() throws IllegalAccessException, InstantiationException {
        Object obj = EXTENSION_INSTANCE.get(type);
        if (obj == null) {
            EXTENSION_INSTANCE.putIfAbsent(type, newInstance());
            obj = EXTENSION_INSTANCE.get(type);
        }

        return (T) obj;
    }
}
