package com.jingl.common.exceptions;

/**
 * Created by Ben on 13/02/2018.
 */
public class SerializeException extends Exception {
    public SerializeException(Throwable throwable) {
        super("序列化/反序列化失败", throwable);
    }
}
