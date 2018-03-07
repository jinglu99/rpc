package com.jingl.proxy;

import com.jingl.common.entity.Request;
import com.jingl.common.entity.Response;
import com.jingl.handle.RequestHandle;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Ben on 27/11/2017.
 */
public class RPCProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Request request = new Request();
        request.setInterfaceName(o.getClass().getInterfaces()[0].getName());
        request.setMethodName(method.getName());
        request.setParams(objects);
        request.setTypes(method.getParameterTypes());

        RequestHandle handle = new RequestHandle();

        Response rep = (Response) handle.invoke(request);

        return rep.getResponse();
    }
}
