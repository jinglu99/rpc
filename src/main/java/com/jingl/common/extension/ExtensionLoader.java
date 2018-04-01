package com.jingl.common.extension;

import com.jingl.common.Constants;
import com.jingl.common.annotation.Impl;
import com.jingl.common.exceptions.FailToInitInstance;
import com.jingl.common.exceptions.NoImplClassFoundException;
import com.jingl.common.exceptions.NotInterfaceExcetption;
import com.jingl.transfer.ExportTransfer;
import com.jingl.transfer.Transfer;
import com.jingl.utils.ClassHelper;
import com.jingl.utils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
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

    public static ExtensionLoader getExtensionLoder(Class clazz) {
        if (!clazz.isInterface()){
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

    private ExtensionLoader(Class<?> type) {
        this.INTERFACE = type;
    }

    /**
     * 创建新实例
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public T newInstance() throws  NoImplClassFoundException {
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
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public T getActiveInstance() {
        Object obj = EXTENSION_INSTANCE.get(INTERFACE);
        if (obj == null) {
            EXTENSION_INSTANCE.putIfAbsent(INTERFACE, newInstance());
            obj = EXTENSION_INSTANCE.get(INTERFACE);
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
            Set<Class> clazzs= ClassHelper.scan(packageName, x->{
                String name = x.getSimpleName();
                return StringUtils.equals(name, finalClazzName);
            });
            if (clazzs.size()==1) {
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

    /**
     * 获得自定义配置
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
        Transfer clazz = (Transfer) ExtensionLoader.getExtensionLoder(ExportTransfer.class).getActiveInstance();
        System.out.println(clazz.getClass().getName());
    }
}
