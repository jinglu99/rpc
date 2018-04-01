package com.jingl.proxy;

import com.jingl.common.entity.Invocation;
import com.jingl.common.entity.Request;
import com.jingl.common.entity.Response;
import com.jingl.handle.Invoker;
import com.jingl.handle.handlers.RequestHandle;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Created by Ben on 27/11/2017.
 */
public class RPCProxy implements MethodInterceptor {
    private static final Logger logger = Logger.getLogger(RPCProxy.class);

    private final Invoker invoker;

    public RPCProxy(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Invocation invocation = new Invocation();

        Request request = invocation.getRequest();
        request.setInterfaceName(o.getClass().getInterfaces()[0].getName());
        request.setMethodName(method.getName());
        request.setParams(objects);
        request.setTypes(method.getParameterTypes());

        RequestHandle handle = new RequestHandle();

        Object result = handle.invoke(request);

        return result;
    }
}
