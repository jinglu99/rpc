package com.jingl.rpc.container;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Provider;
import com.jingl.rpc.common.entity.ServiceContainer;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ServiceExportFailedException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.FailToGenerateInvokerException;
import com.jingl.rpc.pools.ProviderPool;
import com.jingl.rpc.protocol.Protocol;
import com.jingl.rpc.register.Register;
import com.jingl.rpc.utils.PropertyUtils;
import com.jingl.rpc.exchanger.ExportExchanger;
import com.jingl.rpc.utils.ClassHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/12/2017.
 */
public class Container {
    private static Logger logger = Logger.getLogger(Container.class);

    private static int PORT = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_PORT));

    private static ExportExchanger exchanger = (ExportExchanger) ExtensionLoader.getExtensionLoader(ExportExchanger.class).getActiveInstance();

    private static Register register = (Register) ExtensionLoader.getExtensionLoader(Register.class).getActiveInstance();

    private static Protocol protocol = (Protocol) ExtensionLoader.getExtensionLoader(Protocol.class, "provider").getActiveInstance();

    private static ConcurrentHashMap<Class, ServiceContainer> container = new ConcurrentHashMap();

    public static Object getInstance(Class clazz) {
        return container.get(clazz).getInstance();
    }

    public static void setInstance(Class clazz, ServiceContainer obj) {
        container.put(clazz, obj);
    }

    public static int init() throws ServiceExportFailedException {
        logger.info("Start initialize Provider Container");

        loadServiceWithAnnotation();    //扫描注解
        try {
            Invoker handler = protocol.getInvoker();
            ProviderPool.setNextInvoker(handler);
        } catch (FailToGenerateInvokerException e) {
            throw new ServiceExportFailedException(e);
        }
        exchanger.export();
        register.connect();
        return 0;
    }

    public static void loadServiceWithAnnotation() {
        String packageName = PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_PACKAGE);
        if (StringUtils.isBlank(packageName)) {
            logger.info("Provider package name is not set, skip package scanning");
            return;
        }

        logger.info("Start scanning package: " + packageName);

        Set<Class> classes = ClassHelper.scan(packageName, x->{
            Provider annotation = (Provider) x.getAnnotation(Provider.class);
            return annotation != null;
        });

        for (Class clazz : classes) {
            try {
                logger.info("Add provider: " + clazz.getName());
                setInstance(clazz.getInterfaces()[0], new ServiceContainer(clazz.newInstance()));
            } catch (Exception e) {
                logger.warn("Can't initialize Provider: " + clazz.getName());
            }
        }
    }

    public static List<URL> getExportService() throws UnknownHostException {
        List list = new ArrayList();
        for (ServiceContainer service : container.values()) {
            list.add(service.getURL());
        }
        return list;
    }
}
