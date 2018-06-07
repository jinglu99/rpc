package com.jingl.rpc.handle.invokers;

/**
 * Created by Ben on 2018/5/19.
 */
public class FailToGenerateInvokerException extends Exception {
    public FailToGenerateInvokerException(Throwable e) {
        super("Fail to generate Invoker chain!", e);
    }
}
