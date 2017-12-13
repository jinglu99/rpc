package com.jingl.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Ben on 26/11/2017.
 */
public class LCRPCRequest implements Serializable {
    private static final long serialVersionUID = 520L;

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

    @Override
    public String toString() {
        return "LCRPCRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", version='" + version + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
