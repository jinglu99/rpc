package com.jingl.serializer;

import com.jingl.common.exceptions.SerializeException;

/**
 * Created by Ben on 13/02/2018.
 */
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
