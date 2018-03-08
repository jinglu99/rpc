package com.jingl.utils;

import com.jingl.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 07/03/2018.
 */
public class PropertyUtils {

    private static Logger logger = Logger.getLogger(PropertyUtils.class);

    private static volatile Properties properties = null;

    public static String getProperty(String key) {

        //系统配置
        String value = System.getProperty(key);
        if (StringUtils.isNotBlank(value)) return value;

        //加载配置文件
        if (properties == null) {
            synchronized (PropertyUtils.class) {
                String path = System.getProperty(Constants.PROPERTY_FILE_NAME);
                if (path == null) {
                    path = Constants.DEFAULT_PROPERTY_FILE;
                }
                logger.info("Use '" + path + "' as property file path");

                properties = loadProperty(path);
            }
        }

        value = properties.getProperty(key);


        String variableName = "DEFAULT_PROPERTY_" + key.replace(".", "_").replace("rpc_", "").toUpperCase();
        try {
            Field field = Constants.class.getField(variableName);
            value = (String) field.get(Constants.class);
        } catch (NoSuchFieldException e) {
            logger.warn("Variable " + key + " not be set");
        } catch (IllegalAccessException e) {
            logger.warn("Variable " + key + " can't be access");
        }

        return value;
    }


    //加载配置文件
    public static Properties loadProperty(String fileName) {
        Properties properties = new Properties();
        //使用绝对路径
        if (fileName.startsWith("/")) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                try {
                    properties.load(input);
                } finally {
                    input.close();
                }
            } catch (Throwable e) {
                logger.warn("Failed to load " + fileName + " file from " + fileName + "(ingore this file): " + e.getMessage(), e);
            }
            return properties;
        }


        //相对路径
        List<URL> list = new ArrayList<URL>();
        try {
            Enumeration<URL> urls = ClassHelper.getClassLoader().getResources(fileName);
            list = new ArrayList<java.net.URL>();
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
        } catch (Throwable t) {
            logger.warn("Fail to load " + fileName + " file: " + t.getMessage(), t);
        }

        logger.info("load " + fileName + " properties file from " + list);

        for (java.net.URL url : list) {
            try {
                Properties p = new Properties();
                InputStream input = url.openStream();
                if (input != null) {
                    try {
                        p.load(input);
                        properties.putAll(p);
                    } finally {
                        try {
                            input.close();
                        } catch (Throwable t) {
                        }
                    }
                }
            } catch (Throwable e) {
                logger.warn("Fail to load " + fileName + " file from " + url + "(ingore this file): " + e.getMessage(), e);
            }
        }

        return properties;
    }
}
