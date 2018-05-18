package com.jingl.rpc.common.extension;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.exceptions.FailToInitInstance;
import com.jingl.rpc.common.exceptions.NoImplClassFoundException;
import com.jingl.rpc.common.exceptions.NotInterfaceExcetption;
import com.jingl.rpc.transfer.Transfer;
import com.jingl.rpc.utils.PropertyUtils;
import com.jingl.rpc.transfer.ExportTransfer;
import com.jingl.rpc.utils.ClassHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/02/2018.
 */
public class ExtensionLoader<T> {
    private static Logger logger = Logger.getLogger(ExtensionLoader.class);

    private final Class<?> INTERFACE;

    private volatile Class implClass = null;


    private final static ConcurrentHashMap<Class, ExtensionLoader> EXTENSION_LODERS = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Class, Object> EXTENSION_INSTANCE = new ConcurrentHashMap<>();

    public static ExtensionLoader getExtensionLoader(Class clazz) {
        if (!clazz.isInterface()) {
            logger.error("ExtensionLoader only initialize Interface");
            throw new NotInterfaceExcetption();
        }

        ExtensionLoader loader = EXTENSION_LODERS.get(clazz);
        if (loader == null) {
            EXTENSION_LODERS.putIfAbsent(clazz, new ExtensionLoader(clazz));
            loader = EXTENSION_LODERS.get(clazz);
        }
        return loader;
    }

    public static ExtensionLoader getExtensionLoader(Class clazz, String name) {
        if (!clazz.isInterface()) {
            logger.error("ExtensionLoader only initialize Interface");
            throw new NotInterfaceExcetption();
        }

        Class impl = getImplClass(clazz, name);
        ExtensionLoader loader = EXTENSION_LODERS.get(impl);
        if (loader == null) {
            EXTENSION_LODERS.putIfAbsent(impl, new ExtensionLoader(clazz, impl));
            loader = EXTENSION_LODERS.get(impl);
        }
        return loader;
    }

    private ExtensionLoader(Class<?> type) {
        this.INTERFACE = type;
    }

    private ExtensionLoader(Class<?> type, Class impl) {
        this.INTERFACE = type;
        this.implClass = impl;
    }

    /**
     * 创建新实例
     *
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public T newInstance() throws NoImplClassFoundException {
        if (implClass == null) {
            synchronized (this) {
                if (implClass == null) {
                    implClass = getImplClass();
                }
            }
        }

        T instance = null;
        try {
            instance = (T) implClass.newInstance();
        } catch (InstantiationException e) {
            throw new FailToInitInstance(e);
        } catch (IllegalAccessException e) {
            throw new FailToInitInstance(e);
        }
        return instance;
    }


    /**
     * 获得单例实例，如果未实例化，则先实例化
     *
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public T getActiveInstance() {
        Object obj = null;
        if (implClass == null || (obj = EXTENSION_INSTANCE.get(implClass)) == null) {
            Object instance = newInstance();
            EXTENSION_INSTANCE.putIfAbsent(implClass, instance);
            obj = EXTENSION_INSTANCE.get(implClass);
        }

        return (T) obj;
    }

    public Class getImplClass() {

        if (withAnnotation(INTERFACE)) {
            String clazzName = null;
            String packageName = Constants.PACKAGE_NAME;

            //尝试获取用户自定义配置
            clazzName = getPropertyValue(INTERFACE);
            if (StringUtils.isBlank(clazzName)) {
                clazzName = getDefaultImpl(INTERFACE);
            }

            //没有找到实现类
            if (StringUtils.isBlank(clazzName)) return null;

            //在com.jingl包下寻找实现类
            final String finalClazzName = clazzName;
            Set<Class> clazzs = ClassHelper.scan(packageName, x -> {
                String name = x.getSimpleName();
                return StringUtils.equals(name, finalClazzName);
            });
            if (clazzs.size() == 1) {
                return (Class) clazzs.toArray()[0];
            } else {
                //无法找到实现类，抛出异常
                if (implClass == null) {
                    logger.error("can't find implement class: " + clazzName);
                    throw new NoImplClassFoundException();
                }
            }
        }
        return null;
    }

    private static boolean withAnnotation(Class clazz) {
        return clazz.isAnnotationPresent(Impl.class);
    }

    /**
     * 获得默认配置
     *
     * @param clazz
     * @return
     */
    private static String getDefaultImpl(Class clazz) {
        Impl impl = (Impl) clazz.getAnnotation(Impl.class);
        String value = impl.value();
        StringBuilder className = new StringBuilder();
        if (StringUtils.isNotBlank(value)) {
            className.append(value.substring(0, 1).toUpperCase());
            className.append(value.substring(1).toLowerCase());
            className.append(clazz.getSimpleName());
        } else
            return null;
        return className.toString();
    }

    private static Class getImplClass(Class clazz, String name) {
        StringBuilder className = new StringBuilder();
        if (StringUtils.isNotBlank(name)) {
            className.append(name.substring(0, 1).toUpperCase());
            className.append(name.substring(1).toLowerCase());
            className.append(clazz.getSimpleName());
        } else
            return null;


        final String finalClazzName = className.toString();
        Set<Class> clazzs = ClassHelper.scan(Constants.PACKAGE_NAME, x -> {
            String simpleName = x.getSimpleName();
            return StringUtils.equals(simpleName, finalClazzName);
        });
        if (clazzs.size() == 1) {
            return (Class) clazzs.toArray()[0];
        } else {
            logger.error("can't find implement class: " + className);
            throw new NoImplClassFoundException();
        }
    }

    /**
     * 获得自定义配置
     *
     * @param clazz
     * @return
     */
    private static String getPropertyValue(Class clazz) {
        Impl impl = (Impl) clazz.getAnnotation(Impl.class);
        if (impl == null) return null;

        String key = impl.property();
        if (StringUtils.isBlank(key)) return null;

        String value = PropertyUtils.getProperty(key);
        StringBuilder className = new StringBuilder();
        if (StringUtils.isNotBlank(value)) {
            className.append(value.substring(0, 1).toUpperCase());
            className.append(value.substring(1).toLowerCase());
            className.append(clazz.getSimpleName());
        } else
            return null;
        return className.toString();
    }

    public static void main(String[] args) throws NotInterfaceExcetption, IllegalAccessException, NoImplClassFoundException, InstantiationException {
        Transfer clazz = (Transfer) ExtensionLoader.getExtensionLoader(ExportTransfer.class).getActiveInstance();
        System.out.println(clazz.getClass().getName());
    }
}
