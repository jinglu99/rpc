package com.jingl.utils;

/**
 * Created by Ben on 07/03/2018.
 */
public class ClassHelper {

    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassHelper.class);
    }

    public static ClassLoader getClassLoader(Class cls) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }
}
