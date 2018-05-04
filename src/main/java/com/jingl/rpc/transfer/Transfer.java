package com.jingl.rpc.transfer;

import com.jingl.rpc.common.exceptions.SendDataFailedException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;

/**
 * Created by Ben on 12/02/2018.
 */
public interface Transfer {
    byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException;

    int close() throws SocketCloseFailedException;
}
