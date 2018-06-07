package com.jingl.rpc.cluster;

import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.entity.Directory;
import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.NoAvailableConnectionException;
import com.jingl.rpc.common.exceptions.NoProviderFoundException;
import com.jingl.rpc.exchanger.Exchanger;
import com.jingl.rpc.handle.Invoker;

/**
 * Created by Ben on 13/02/2018.
 */
@Impl("rpc")
public interface Cluster {

    void init();

    Exchanger getTransfer(Invocation invocation) throws NoProviderFoundException, NoAvailableConnectionException;

    Directory getDirectory(Class clazz);

    void connect(Invoker invoker) throws ConnectionFailedException;
}
