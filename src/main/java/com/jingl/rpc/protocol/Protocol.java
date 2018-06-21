package com.jingl.rpc.protocol;

import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.common.exceptions.FailToGenerateInvokerException;

/**
 * Created by Ben on 2018/4/19.
 */
@Impl("default")
public interface Protocol {
    Invoker getInvoker() throws FailToGenerateInvokerException;
}
