package com.jingl.rpc.common.entity;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.proxy.RPCProxy;
import com.jingl.rpc.utils.PropertyUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Ben on 2018/4/27.
 */
public class ServiceContainer {
    private final Object instance;
    private final Class INTERFACE;
    private volatile URL url = null;

    public ServiceContainer(Object instance) {
        this.instance = instance;
        INTERFACE = instance.getClass().getInterfaces()[0];
    }

    public Object getInstance() {
        return instance;
    }

    public Class getINTERFACE() {
        return INTERFACE;
    }

    public URL getURL() throws UnknownHostException {
        if (url == null) {
            synchronized (this) {
                if (url == null) {
                    String ip = InetAddress.getLocalHost().getHostAddress();
                    url = new URL(Constants.RPC, ip, Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_PROVIDER_PORT)), INTERFACE.getName(), null);
                }
            }
        }
        return url;
    }

    @Override
    public String toString() {
        return "ServiceContainer{" +
                "instance=" + instance +
                '}';
    }
}
