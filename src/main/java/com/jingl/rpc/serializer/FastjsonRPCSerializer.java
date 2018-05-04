package com.jingl.rpc.serializer;

import com.alibaba.fastjson.JSON;

/**
 * Created by Ben on 13/02/2018.
 */
public class FastjsonRPCSerializer implements RPCSerializer {

    @Override
    public byte[] serialize(Object obj) {
        String rsp = JSON.toJSONString(obj);
        return rsp.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class T) {
        Object object = JSON.parseObject(bytes, T);
        return (T) object;
    }
}
