package com.jingl.rpc.serializer;

import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.annotation.Impl;
import com.jingl.rpc.common.exceptions.SerializeException;

/**
 * Created by Ben on 13/02/2018.
 */
@Impl(value = "jdk", property = Constants.PROPERTY_SERIALIZE_TYPE)
public interface RPCSerializer {
    /**
     * 序列化
     * @param obj
     * @return
     */
    byte[] serialize(Object obj) throws SerializeException;

    /**
     * 反序列化
     * @param bytes
     * @param T
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class T) throws SerializeException;
}
