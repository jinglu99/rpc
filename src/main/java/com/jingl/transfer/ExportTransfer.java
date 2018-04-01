package com.jingl.transfer;

import com.jingl.common.Constants;
import com.jingl.common.annotation.Impl;
import com.jingl.common.entity.URL;
import com.jingl.common.exceptions.SendDataFailedException;
import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.common.exceptions.SocketCloseFailedException;
import com.jingl.handle.Handler;
import com.jingl.handle.Invoker;

/**
 * Created by Ben on 25/03/2018.
 */
@Impl(value = "netty",property = Constants.PROPERTY_EXPORT_TRANSFER)
public interface ExportTransfer extends Transfer {

    int export();

    void setParams(int port, Invoker handler) throws ServiceExportFailedException;
}
