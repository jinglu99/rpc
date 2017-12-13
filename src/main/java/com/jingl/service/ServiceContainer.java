package com.jingl.service;

import com.jingl.entity.ServiceInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 26/11/2017.
 */
public class ServiceContainer {
    private static ServiceContainer servicesSingle = null;
    private Map<String,ServiceInfo> services = null;

    private ServiceContainer(){
        services = new ConcurrentHashMap<>();
    }

    public static ServiceContainer getServiceSingle(){
        synchronized (ServiceContainer.class){
            if (servicesSingle == null){
                servicesSingle = new ServiceContainer();
            }
        }
        return servicesSingle;
    }

    public Map<String, ServiceInfo> getServices() {
        return servicesSingle.services;
    }
}
