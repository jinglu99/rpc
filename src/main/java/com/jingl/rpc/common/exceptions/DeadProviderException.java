package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 2018/5/9.
 */
public class DeadProviderException extends Exception {
    public DeadProviderException() {
        super("The Provider is Dead!");
    }
}
