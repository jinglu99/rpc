package com.jingl.rpc.protocol;

import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.handle.Invoker;

/**
 * Created by Ben on 2018/4/19.
 */
@Impl("RPC")
public interface Protocol {
    Invoker getInvoker();
}
