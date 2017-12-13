package com.jingl.proxy;

import com.jingl.container.Container;
import com.jingl.entity.LCRPCRequest;
import com.jingl.entity.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/12/2017.
 */
public class ProviderProxy {

    private Container container = new Container();

    public Response procced(LCRPCRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = Class.forName(request.getInterfaceName());
        Object instance = container.getInstance(clazz);
        Method method = clazz.getMethod(request.getMethodName(), request.getTypes());
        Object result = method.invoke(instance, request.getParams());
        return new Response(result);
    }
}
