package com.jingl.rpc.serializer;

import com.alibaba.fastjson.JSON;
import com.jingl.rpc.common.exceptions.SerializeException;

/**
 * Created by Ben on 13/02/2018.
 */
public class FastjsonRPCSerializer implements RPCSerializer {

    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        try {

            String rsp = JSON.toJSONString(obj);
            return rsp.getBytes();
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class T) throws SerializeException {
        try {
            Object object = JSON.parseObject(bytes, T);
            return (T) object;
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }
}
