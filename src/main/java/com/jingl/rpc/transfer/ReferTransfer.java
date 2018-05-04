package com.jingl.rpc.transfer;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.handle.Invoker;

/**
 * Created by Ben on 25/03/2018.
 */
@Impl(value = "netty",property = Constants.PROPERTY_EXPORT_TRANSFER)
public interface ReferTransfer extends Transfer {

    int refer();

    void setParams(URL url, Invoker invoker) throws ConnectionFailedException;
}
