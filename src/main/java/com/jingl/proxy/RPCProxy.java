package com.jingl.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.jingl.entity.LCRPCRequest;
import com.jingl.entity.Request;
import com.jingl.entity.Response;
import com.jingl.handle.RequestHandle;
import com.sun.deploy.net.proxy.ProxyHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Ben on 27/11/2017.
 */
public class RPCProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        LCRPCRequest request = new LCRPCRequest();
        request.setInterfaceName(o.getClass().getInterfaces()[0].getName());
        request.setMethodName(method.getName());
        request.setParams(objects);
        request.setTypes(method.getParameterTypes());

        RequestHandle handle = new RequestHandle(request);
        handle.createSocket();

        Response rep = handle.proceed();

        return rep.getResponse();
    }
}
