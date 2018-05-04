package com.jingl.rpc.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Ben on 07/03/2018.
 */
public class ClassHelper {

    private static Logger logger = Logger.getLogger(ClassHelper.class);

    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassHelper.class);
    }

    public static ClassLoader getClassLoader(Class cls) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
            logger.warn("Cannot access thread context ClassLoader - falling back to system class loader...");
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            logger.info("use class loader of this class.");
            cl = cls.getClassLoader();
        }
        return cl;
    }

    /**
     * 扫描
     */
    public static Set<Class> scan(String packageName) {
        return scan(packageName, x->true);
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    public static Set<Class> scan(String packageName, Function<Class, Boolean> function) {

        Set<Class> classes = new HashSet<>();

        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs = null;
        try {
            dirs = Thread.currentThread().getContextClassLoader()
                    .getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, classes, function);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    findAndAddClassesInPackageByJAR(packageName, packageDirName, url, classes, function);
                }
            }
        } catch (IOException e) {
            System.err.println("扫描出错");
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                  String packagePath,
                                                  Set<Class> classes,
                                                  Function<Class, Boolean> function) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(), classes, function);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    Class<?> clazz = Thread.currentThread().getContextClassLoader()
                            .loadClass(packageName + '.' + className);
                    addClass(classes, clazz, function);
                } catch (ClassNotFoundException e) {
                    System.err.println("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以Jar的形式来获取包下的所有Class
     * @param packageName
     * @param packageDirName
     * @param url
     * @param classes
     * @param function
     */
    private static void findAndAddClassesInPackageByJAR(String packageName,
                                                 String packageDirName,
                                                 URL url,
                                                 Set<Class> classes,
                                                 Function<Class, Boolean> function) {
        // 定义一个JarFile
        JarFile jar = null;
        try {
            // 获取jar
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            // 从此jar包 得到一个枚举类
            Enumeration<JarEntry> entries = jar.entries();
            // 同样的进行循环迭代
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // 如果是以/开头的
                if (name.charAt(0) == '/') {
                    // 获取后面的字符串
                    name = name.substring(1);
                }
                // 如果前半部分和定义的包名相同
                if (name.startsWith(packageDirName)) {
                    int idx = name.lastIndexOf('/');
                    // 如果以"/"结尾 是一个包
                    if (idx != -1) {
                        // 获取包名 把"/"替换成"."
                        packageName = name.substring(0, idx).replace('/',
                                '.');
                    }
                    // 如果可以迭代下去 并且是一个包
                    if ((idx != -1) || true) {
                        // 如果是一个.class文件 而且不是目录
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            // 去掉后面的".class" 获取真正的类名
                            String className = name.substring(
                                    packageName.length() + 1,
                                    name.length() - 6);
                            try {
                                // 添加到classes
                                // 使用Class.forName会触发类静态方法
                                Class<?> clazz = Thread.currentThread()
                                        .getContextClassLoader().loadClass(
                                                packageName + '.' + className);
                                addClass(classes, clazz, function);
                            } catch (ClassNotFoundException e) {
                                logger.error("加载类出错");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("在扫描用户定义视图时从jar包获取文件出错");
            e.printStackTrace();
        }
    }


    private static void addClass(Set<Class> classes, Class clazz, Function<Class, Boolean> function) {
        if (function.apply(clazz)) {
            classes.add(clazz);
        }
    }

    public static void main(String[] args) {
        Set<Class> classes = ClassHelper.scan("/", x->{
            String name= x.getName();
            return name.endsWith("r");
        });
        for (Class clazz : classes) {
            System.out.println(clazz.getName());
        }
    }

}
