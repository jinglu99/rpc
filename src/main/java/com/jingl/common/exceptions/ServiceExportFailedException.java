package com.jingl.common.exceptions;

/**
 * Created by Ben on 12/02/2018.
 */
public class ServiceExportFailedException extends Exception {
    public ServiceExportFailedException(Throwable throwable) {
        super("服务发布失败!", throwable);
    }
}
