package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 12/02/2018.
 */
public class SocketCloseFailedException extends Exception {
    public SocketCloseFailedException(Throwable throwable) {
        super("socket无法关闭", throwable);
    }

}
