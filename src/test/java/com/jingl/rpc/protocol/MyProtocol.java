package com.jingl.rpc.protocol;

import com.esotericsoftware.minlog.Log;
import com.jingl.rpc.common.exceptions.NotInvokerException;
import jdk.nashorn.internal.runtime.options.LoggingOption;

/**
 * Created by Ben on 2018/5/19.
 */
public class MyProtocol extends ProtocolTemplate {

    protected void init() throws NotInvokerException {
        beforeSend().add(LogInvoker.class);

        afterSend().add(LogInvoker.class);

        beforeReturn().add(LogInvoker.class);

        afterReturn().add(LogInvoker.class);

        beforeMethod().add(LogInvoker.class);

        afterMethod().add(LogInvoker.class);

        afterResponse().add(LogInvoker.class);
    }
}
