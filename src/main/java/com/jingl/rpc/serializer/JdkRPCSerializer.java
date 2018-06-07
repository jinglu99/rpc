package com.jingl.rpc.serializer;

import com.jingl.rpc.common.exceptions.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 使用jdk自带序列化
 * @author lindezhi
 * 2016年6月13日 下午4:25:03
 */
public class JdkRPCSerializer implements RPCSerializer {

	/**
	 * 先序列化再执行压缩，减少网络流量
	 */
	@Override
	public byte[] serialize(Object obj) throws SerializeException {
		try {
			ByteArrayOutputStream bis = new ByteArrayOutputStream();
			ObjectOutputStream stream = new ObjectOutputStream(bis);
			stream.writeObject(obj);
			stream.close();
			byte[] bytes = bis.toByteArray();
			return bytes;
		} catch (Exception e) {
			throw new SerializeException(e);
		}
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class T) throws SerializeException {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream stream = new ObjectInputStream(bis);
			return (T)stream.readObject();
		} catch (Exception e) {
			throw new SerializeException(e);
		}
	}
}
