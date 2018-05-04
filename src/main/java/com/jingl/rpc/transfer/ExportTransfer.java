package com.jingl.rpc.transfer;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.exceptions.ServiceExportFailedException;
import com.jingl.rpc.handle.Invoker;

/**
 * Created by Ben on 25/03/2018.
 */
@Impl(value = "netty",property = Constants.PROPERTY_EXPORT_TRANSFER)
public interface ExportTransfer extends Transfer {

    int export();

    void setParams(int port, Invoker handler) throws ServiceExportFailedException;
}
