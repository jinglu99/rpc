package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Invoker;

/**
 * 处理服务端异常
 * Created by Ben on 2018/4/23.
 */
public class ExceptionInvoker implements Invoker {

    private final Invoker next;

    public ExceptionInvoker(Invoker invoker) {
        this.next = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Request request = null;
        try {
            request = (Request) val;
            return next.invoke(val);
        } catch (InvokerException e) {
            Response response = new Response();
            response.setId(request.getId());
            response.setException(e.getCause());
            return response;
        }
    }
}
