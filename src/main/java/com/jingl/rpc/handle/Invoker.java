package com.jingl.rpc.handle;

import com.jingl.rpc.common.exceptions.InvokerException;

/**
 * Created by Ben on 13/02/2018.
 */
public interface Invoker {
    Object invoke(Object val) throws InvokerException;
}
