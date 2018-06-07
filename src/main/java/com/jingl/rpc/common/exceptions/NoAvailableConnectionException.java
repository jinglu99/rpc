package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 2018/5/8.
 */
public class NoAvailableConnectionException extends Exception {
    public NoAvailableConnectionException() {
        super("No available connection found!");
    }
}
