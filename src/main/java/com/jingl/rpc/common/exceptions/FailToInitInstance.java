package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 25/03/2018.
 */
public class FailToInitInstance extends RuntimeException {
    public FailToInitInstance(Throwable throwable) {
        super("fail to init instance", throwable);
    }
}
