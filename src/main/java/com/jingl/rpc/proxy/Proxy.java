package com.jingl.rpc.proxy;

import com.jingl.rpc.common.exceptions.FailToGenerateInvokerException;

/**
 * Created by Ben on 13/02/2018.
 */
public interface Proxy {
    <T> T getInstance(Class T) throws FailToGenerateInvokerException;
}
