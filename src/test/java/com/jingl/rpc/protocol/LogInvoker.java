package com.jingl.rpc.protocol;

import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.handle.Invoker;

/**
 * Created by Ben on 2018/5/19.
 */
public class LogInvoker implements Invoker {
    public final Invoker next;

    public LogInvoker(Invoker next) {
        this.next = next;
    }


    public Object invoke(Object o) throws InvokerException {
        System.out.println(o);
        if (next != null)
            next.invoke(o);
        return null;
    }
}
