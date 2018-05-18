package com.jingl.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jingl.rpc.common.entity.Request;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.exceptions.SerializeException;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Ben on 2018/5/9.
 */
public class KryoRPCSerializer implements RPCSerializer {
    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {//<3>
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setReferences(true);//默认值为true,强调作用
            kryo.setRegistrationRequired(false);//默认值为false,强调作用
            return kryo;
        }
    };

    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        Output output = null;
        try {
            Kryo kryo = kryoLocal.get();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            output = new Output(byteArrayOutputStream);//<1>
            kryo.writeClassAndObject(output, obj);//<2>
            output.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializeException(e);
        } finally {
            try {
                output.close();
            } catch (Exception e) {}
        }

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class T) throws SerializeException {
        Input input = null;
        try {
            Kryo kryo = kryoLocal.get();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            input = new Input(byteArrayInputStream);// <1>
            input.close();
            return (T) kryo.readClassAndObject(input);//<2>
        } catch (Exception e) {
            throw new SerializeException(e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {}
        }

    }

    public static void main(String[] args) throws SerializeException {
        Request request = new Request();
        request.setId(System.currentTimeMillis());
        KryoRPCSerializer serializer = new KryoRPCSerializer();
        byte[] bytes = serializer.serialize(request);

        request = serializer.deserialize(bytes, Request.class);
        System.out.println(request.toString());
    }
}
