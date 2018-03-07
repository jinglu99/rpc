package com.jingl.common.exceptions;

/**
 * Created by Ben on 12/02/2018.
 */
public class ConnectionFailedException extends Exception {
    public ConnectionFailedException(Throwable cause) {
        super("无法建立socket连接", cause);
    }
}
