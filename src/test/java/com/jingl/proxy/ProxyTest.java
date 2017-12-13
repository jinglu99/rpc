package com.jingl.proxy;

/**
 * Created by Ben on 27/11/2017.
 */
public class ProxyTest  {
    public static void main(String[] args) {
        RemoteInterface instance = ProxyFactory.getInstance(RemoteInterface.class);
        System.out.print(instance.func("1", 1, 1.0));
        Data data = new Data();
//        instance.func(data);
    }
}
