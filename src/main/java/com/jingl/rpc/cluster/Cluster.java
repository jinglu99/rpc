package com.jingl.rpc.cluster;

import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.entity.Directory;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.NoAvailableConnectionException;
import com.jingl.rpc.common.exceptions.NoProviderFoundException;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.Transfer;

/**
 * Created by Ben on 13/02/2018.
 */
@Impl("test")
public interface Cluster {

    void init();

    Transfer getTransfer(Class clazz) throws NoProviderFoundException, NoAvailableConnectionException;

    Directory getDirectory(Class clazz);

    void connect(Invoker invoker) throws ConnectionFailedException;
}
