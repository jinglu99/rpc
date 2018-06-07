package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 13/02/2018.
 */
public class InvokerException extends Exception {
    public InvokerException(Throwable throwable) {
        super("Invoker异常", throwable);
    }
}
