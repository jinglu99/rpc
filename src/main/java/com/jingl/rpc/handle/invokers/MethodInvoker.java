package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.container.Container;
import com.jingl.rpc.handle.Invoker;

import java.lang.reflect.Method;

/**
 * Created by Ben on 2018/4/23.
 */
public class MethodInvoker implements Invoker {

    private final Invoker before;
    private final Invoker after;

    public MethodInvoker(Invoker before, Invoker after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        try {
            Request request = (Request) val;
            if (before != null)
                before.invoke(request);

            Class clazz = Class.forName(request.getInterfaceName());
            Container container = new Container();
            Object instance = container.getInstance(clazz);
            Method method = clazz.getMethod(request.getMethodName(), request.getTypes());
            Object result = null;

            result = method.invoke(instance, request.getParams());

            Response response = new Response();
            response.setId(request.getId());
            response.setResponse(result);

            if (after != null)
                after.invoke(response);
            return response;
        } catch (Exception e) {
            throw new InvokerException(e);
        }

    }
}
