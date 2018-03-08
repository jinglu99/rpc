package com.jingl.server.service;

import com.jingl.common.annotation.Provider;
import com.jingl.proxy.RemoteInterface;

/**
 * Created by Ben on 08/03/2018.
 */
@Provider("implement1")
public class Implement1 implements RemoteInterface{
    @Override
    public String func(Object var1, Object var2, Object var3) {
        System.out.println("func(var1, var2, var3) called");
        return "called success";
    }
}
