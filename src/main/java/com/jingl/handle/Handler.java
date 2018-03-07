package com.jingl.handle;

import com.jingl.common.exceptions.InvokerException;

/**
 * Created by Ben on 13/02/2018.
 */
public interface Handler {
    Object invoke(Object val) throws InvokerException;
}
