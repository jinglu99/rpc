package com.jingl.service;

import com.jingl.entity.ServiceInfo;

import java.util.Set;

/**
 * Created by Ben on 26/11/2017.
 */
public class ServiceAccess {
    public boolean serviceRegistry(ServiceInfo serviceInfo) {
        if (serviceInfo.getInterfaceName() == null || serviceInfo.getInterfaceName().length() ==0 ||
                serviceInfo.getImplClassName() == null || serviceInfo.getImplClassName().length() ==0 ||
                serviceInfo.getVersion() == null || serviceInfo.getVersion().length() ==0 ||
                serviceInfo.getIp() == null || serviceInfo.getIp().length() ==0)
            return false;
        String serviceID = serviceInfo.getInterfaceName() + "_" + serviceInfo.getVersion();
        if (ServiceContainer.getServiceSingle().getServices().containsKey(serviceID)){
            ServiceContainer.getServiceSingle().getServices().get(serviceID).getIps().add(serviceInfo.getIp());
        }else {
            serviceInfo.getIps().add(serviceInfo.getIp());
            ServiceContainer.getServiceSingle().getServices().put(serviceID,serviceInfo);
        }
        return true;
    }

    public Set<String> queryServiceIPsByID(String serviceID) {
        if (!ServiceContainer.getServiceSingle().getServices().containsKey(serviceID))
            return null;


        return ServiceContainer.getServiceSingle().getServices().get(serviceID).getIps();
    }

    public ServiceInfo queryServiceInfoByID(String serviceID) {
        if (!ServiceContainer.getServiceSingle().getServices().containsKey(serviceID))
            return null;


        return ServiceContainer.getServiceSingle().getServices().get(serviceID);
    }
}
