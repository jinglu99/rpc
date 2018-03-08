package com.jingl.container;

import com.jingl.common.Constants;
import com.jingl.common.annotation.Provider;
import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.handle.Handler;
import com.jingl.handle.ProviderHandle;
import com.jingl.transfer.SocketExportTransfer;
import com.jingl.transfer.Transfer;
import com.jingl.utils.ClassHelper;
import com.jingl.utils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/12/2017.
 */
public class Container {
    private static Logger logger = Logger.getLogger(Container.class);

    private static ConcurrentHashMap<Class, Object> container = new ConcurrentHashMap();

    private static int PORT = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_PORT));

    public Object getInstance(Class clazz) {
        return container.get(clazz);
    }

    public void setInstance(Class clazz, Object obj) {
        container.put(clazz, obj);
    }

    public int init() throws ServiceExportFailedException {
        logger.info("Start initialize Provider Container");

        loadServiceWithAnnotation();    //扫描注解

        Handler handler = new ProviderHandle();
        Transfer transfer = new SocketExportTransfer(2532, handler);
        transfer.export();
        return 0;
    }

    public void loadServiceWithAnnotation() {
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
                setInstance(clazz, clazz.newInstance());
            } catch (Exception e) {
                logger.warn("Can't initialize Provider: " + clazz.getName());
            }
        }
    }
}
