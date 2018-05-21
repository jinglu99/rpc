package com.jingl.rpc.server.service;

import com.jingl.rpc.common.annotation.Provider;
import com.jingl.rpc.proxy.RemoteInterface;

/**
 * Created by Ben on 08/03/2018.
 */
@Provider("implement1")
public class Implement1 implements RemoteInterface{
    @Override
    public long func(Object var1, Object var2, Object var3) throws Exception {
        System.out.println("func(var1, var2, var3) called");
        for (int i= 10000; i>0; i--);
//        throw new Exception("test exception");
        return System.currentTimeMillis();
    }
}
