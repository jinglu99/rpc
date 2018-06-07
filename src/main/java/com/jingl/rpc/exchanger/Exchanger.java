package com.jingl.rpc.exchanger;

import com.jingl.rpc.common.exceptions.SendDataFailedException;
import com.jingl.rpc.common.exceptions.SocketCloseFailedException;

/**
 * Created by Ben on 12/02/2018.
 */
public interface Exchanger {
    byte[] send(byte[] data) throws SendDataFailedException, SocketCloseFailedException;

    boolean isActive();

    int close() throws SocketCloseFailedException;
}
