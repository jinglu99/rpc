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


    public static final String PROPERTY_CONSUMER_CONNECTIONS = "rpc.consumer.connections";
    public static final String PROPERTY_CONSUMER_RETRY = "rpc.consumer.retry";
    public static final String PROPERTY_CONSUMER_PROTOCOL = "rpc.consumer.protocol";
    public static final String PROPERTY_CONSUMER_LOAD = "rpc.consumer.load";

    public static final String PROPERTY_EXPORT_EXCHANGER = "rpc.provider.exchanger";
    public static final String PROPERTY_PROVIDER_TIMEOUT = "rpc.provider.timeout";
    public static final String PROPERTY_PROVIDER_THREAD = "rpc.provider.thread";
    public static final String PROPERTY_PROVIDER_PACKAGE = "rpc.provider.package";
    public static final String PROPERTY_PROVIDER_PORT = "rpc.provider.port";
    public static final String PROPERTY_PROVIDER_PROTOCOL = "rpc.provider.protocol";


    public static final String PROPERTY_NETTY_RETRY = "rpc.netty.retry";
    public static final String PROPERTY_NETTY_INTERVAL = "rpc.netty.interval";

    public static final String PROPERTY_SERIALIZE_TYPE = "rpc.serialize.type";

    public static final String PROPERTY_PACKAGES = "rpc.packages";


    //==============================
    //Properties： default value
    //==============================
    public static final String DEFAULT_PROPERTY_FILE = "rpc.properties";
    public static final String DEFAULT_PROPERTY_APPLICATION_NAME = "rpcApp";

    public static final String DEFAULT_PROPERTY_REGISTER_URL = "127.0.0.1";
    public static final String DEFAULT_PROPERTY_REGISTER_PORT = "2181";
    public static final String DEFAULT_PROPERTY_REGISTER_ROOT = "rpc";
    public static final String DEFAULT_PROPERTY_REGISTER_TYPE = "zookeeper";

    public static final String DEFAULT_PROPERTY_CONSUMER_CONNECTIONS = "1";
    public static final String DEFAULT_PROPERTY_CONSUMER_PROTOCOL = "default";
    public static final String DEFAULT_PROPERTY_CONSUMER_RETRY = "3";

    public static final String DEFAULT_PROPERTY_PROVIDER_TIMEOUT = "3000";
    public static final String DEFAULT_PROPERTY_PROVIDER_THREAD = "10";
    public static final String DEFAULT_PROPERTY_PROVIDER_PROTOCOL = "default";
    public static final String DEFAULT_PROPERTY_PROVIDER_PORT = "2532";

    public static final String DEFAULT_PROPERTY_NETTY_RETRY = "3";
    public static final String DEFAULT_PROPERTY_NETTY_INTERVAL = "1000";




    //==============================
    //Constant value
    //==============================
    public static final String RPC = "rpc";

}