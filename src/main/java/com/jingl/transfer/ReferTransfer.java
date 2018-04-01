package com.jingl.transfer;

import com.jingl.common.Constants;
import com.jingl.common.annotation.Impl;
import com.jingl.common.entity.URL;
import com.jingl.common.exceptions.ConnectionFailedException;
import com.jingl.handle.Invoker;
import com.jingl.transfer.Transfer;

/**
 * Created by Ben on 25/03/2018.
 */
@Impl(value = "socket",property = Constants.PROPERTY_EXPORT_TRANSFER)
public interface ReferTransfer extends Transfer {

    int refer();

    void setParams(URL url, Invoker invoker) throws ConnectionFailedException;
}
