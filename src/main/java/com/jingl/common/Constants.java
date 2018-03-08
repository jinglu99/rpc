package com.jingl.common;

/**
 * Created by Ben on 26/11/2017.
 */
public class Constants {
    public static final String IP = "127.0.0.1";//本机地址
    public static final int PORT = 8082;
    public static final String SERVICEIDILLEGAL = "接口名称或者版本号不合法";
    public static final String SERVICENOTFOUND = "未找到该服务";
    public static final String SERVICEMETHODILLEGAl = "方法名称不合法";
    public static final String SERVICECALLEXCEPTION = "LCRPC框架远程调用时出现异常";
    public static final String SERVICEUNKNOWNEXCEPTION = "未知异常";
    public static final String LISTENFAILED = "服务开启监听失败";
    public static final String PUBLISHSERVICEINDONOTCOMPLETE = "发布服务信息不完整";
    public static final String SERVICEREGISTRYFAILED = "发布服务注册失败";

    public static final String SERVICEACCESSCENTER_IP = "127.0.0.1";//服务注册查找中心ip地址
    public static final int SERVICEACCESSCENTER_PORT = 8080;//服务注册查找中心端口号
    public static final String SERVICEREGISTRY = "/ServiceAccessCenter/serviceRegistry.do";//服务注册查找中心注册接口URL
    public static final String QUERYSERVICEIPSBYID = "/ServiceAccessCenter/queryServiceIPsByID.do";//服务注册查找中心ip地址列表查询接口URL
    public static final String QUERYSERVICEINFOBYID = "/ServiceAccessCenter/queryServiceInfoByID.do";//服务注册查找中心服务信息查询接口URL

    //==============================
    //Properties： keys
    //==============================
    public static final String PROPERTY_FILE_NAME = "rpc.properties.file";

    public static final String PROPERTY_APPLICATION_NAME = "rpc.application.name";

    public static final String PROPERTY_REGISTER_URL = "rpc.register.url";
    public static final String PROPERTY_REGISTER_PORT = "rpc.register.port";

    public static final String PROPERTY_PROVIDER_PACKAGE = "rpc.provider.package";
    public static final String PROPERTY_PROVIDER_PORT = "rpc.provider.port";







    //==============================
    //Properties： default value
    //==============================
    public static final String DEFAULT_PROPERTY_FILE = "rpc.properties";
    public static final String DEFAULT_PROPERTY_APPLICATION_NAME = "rpcApp";
    public static final String DEFAULT_PROPERTY_REGISTER_URL = "127.0.0.1";
    public static final String DEFAULT_PROPERTY_REGISTER_PORT = "2181";
    public static final String DEFAULT_PROPERTY_PROVIDER_PORT = "2532";
}