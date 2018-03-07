package com.jingl.transfer;

import com.jingl.common.exceptions.SendDataFailedException;
import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.common.exceptions.SocketCloseFailedException;

/**
 * Created by Ben on 12/02/2018.
 */
public interface Transfer {
    byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException;

    int export();

    int close() throws SocketCloseFailedException;
}
