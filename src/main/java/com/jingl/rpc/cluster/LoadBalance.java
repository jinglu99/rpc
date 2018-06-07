package com.jingl.rpc.cluster;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.entity.Invocation;

import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 2018/5/21.
 */
@Impl(value = "random", property = Constants.PROPERTY_CONSUMER_LOAD)
public interface LoadBalance {
    Object select(Invocation invocation, Set urls);
}
