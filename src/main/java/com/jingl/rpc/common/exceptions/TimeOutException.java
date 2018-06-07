package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 2018/5/26.
 */
public class TimeOutException extends Exception {
    public TimeOutException() {
        super("time out to call remote interface!");
    }
}
