package com.jingl.rpc.register;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Impl;

/**
 * Created by Ben on 2018/4/27.
 */
@Impl(value = "zookeeper", property = Constants.PROPERTY_REGISTER_TYPE)
public interface Register {
    void connect();

    void set(String path, String value);


}
