package com.jingl.rpc.common;

/**
 * Created by Ben on 26/11/2017.
 */
public class Constants {
    public static final String PACKAGE_NAME = "com.jingl";


    public static final String PROPERTY_PROFIX = "rpc.";
    public static final String PROPERTY_PROVIDER_PROFIX = "rpc.provider.";
    public static final String PROPERTY_CONSUMER_PROFIX = "rpc.consumer.";

    //==============================
    //Properties： keys
    //==============================
    public static final String PROPERTY_FILE_NAME = "rpc.properties.file";

    public static final String PROPERTY_APPLICATION_NAME = "rpc.application.name";

    public static final String PROPERTY_REGISTER_TYPE = "rpc.register.type";
    public static final String PROPERTY_REGISTER_URL = "rpc.register.url";
    public static final String PROPERTY_REGISTER_PORT = "rpc.register.port";
    public static final String PROPERTY_REGISTER_ROOT = "rpc.register.root";

    public static final String PROPERTY_PROVIDER_PACKAGE = "rpc.provider.package";
    public static final String PROPERTY_PROVIDER_PORT = "rpc.provider.port";
    public static final String PROPERTY_CONSUMER_CONNECTIONS = "rpc.consumer.connections";
    public static final String PROPERTY_CONSUMER_RETRY = "rpc.consumer.retry";


    public static final String PROPERTY_EXPORT_TRANSFER = "rpc.provider.transfer";




    //==============================
    //Properties： default value
    //==============================
    public static final String DEFAULT_PROPERTY_FILE = "rpc.properties";
    public static final String DEFAULT_PROPERTY_APPLICATION_NAME = "rpcApp";
    public static final String DEFAULT_PROPERTY_REGISTER_URL = "127.0.0.1";
    public static final String DEFAULT_PROPERTY_REGISTER_PORT = "2181";
    public static final String DEFAULT_PROPERTY_REGISTER_ROOT = "rpc";
    public static final String DEFAULT_PROPERTY_PROVIDER_PORT = "2532";
    public static final String DEFAULT_PROPERTY_CONSUMER_CONNECTIONS = "1";
    public static final String DEFAULT_PROPERTY_REGISTER_TYPE = "zookeeper";
    public static final String DEFAULT_PROPERTY_CONSUMER_RETRY = "3";



    //==============================
    //Constant value
    //==============================
    public static final String RPC = "rpc";

}