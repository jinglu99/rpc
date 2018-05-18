package com.jingl.rpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingl.rpc.common.exceptions.SerializeException;

/**
 * Created by Ben on 2018/5/9.
 */
public class JacksonRPCSerializer implements RPCSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class T) throws SerializeException {
        try {
            return (T) mapper.readValue(bytes,T);
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }
}
