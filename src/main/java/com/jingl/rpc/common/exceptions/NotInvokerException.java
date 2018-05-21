package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 2018/5/19.
 */
public class NotInvokerException extends Exception {
    public NotInvokerException(Class clazz) {
        super(clazz.getName() + " is not a Invoker");
    }
}
