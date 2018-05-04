package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 2018/5/1.
 */
public class NoProviderFoundException extends Exception {
    public NoProviderFoundException() {
        super("can't find provider in register");
    }
}
