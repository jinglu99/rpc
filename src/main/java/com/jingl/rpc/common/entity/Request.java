package com.jingl.rpc.common.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Ben on 26/11/2017.
 */
public class Request implements Serializable{
    private static final long serialVersionUID = 520L;

    private long id;

    private String interfaceName;//服务对应接口名称
    private String version;//版本号
    private String methodName;//调用方法名称
    private Class[] types;
    private Object[] params;//调用方法参数

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + id + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", version='" + version + '\'' +
                ", methodName='" + methodName + '\'' +
                ", types=" + Arrays.toString(types) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
