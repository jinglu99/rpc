package com.jingl.rpc.serializer;

import com.jingl.rpc.common.exceptions.SerializeException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;

/**
 * Created by Ben on 2018/5/10.
 */
public class ProtostuffRPCSerializer implements RPCSerializer {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class T) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(T);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(T);
            if (schema != null) {
                cachedSchema.putIfAbsent(T, schema);
            }
        }
        return schema;
    }


    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        Class cls = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class T) throws SerializeException {
        try {
            T message = (T) objenesis.newInstance(T);
            Schema<T> schema = getSchema(T);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
