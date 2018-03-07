package com.jingl.handle;

import com.jingl.cluster.Cluster;
import com.jingl.common.entity.URL;
import com.jingl.common.entity.Request;
import com.jingl.common.entity.Response;
import com.jingl.common.exceptions.ConnectionFailedException;
import com.jingl.common.exceptions.SendDataFailedException;
import com.jingl.common.exceptions.SerializeException;
import com.jingl.common.exceptions.SocketCloseFailedException;
import com.jingl.serializer.FastjsonSerializer;
import com.jingl.serializer.RPCSerializer;
import com.jingl.transfer.SocketReferTransfer;
import com.jingl.transfer.Transfer;

/**
 * Created by Ben on 26/11/2017.
 */
public class RequestHandle implements Handler {

    private Cluster cluster = new Cluster() {
        @Override
        public Transfer getTransfer(Class clazz) throws ConnectionFailedException {
            return new SocketReferTransfer(new URL(null, "localhost", 2532, null, null));
        }
    };

    private RPCSerializer serializer = new FastjsonSerializer();

    @Override
    public Object invoke(Object val) {

        Request request = (Request) val;

        try {
            //获得一个连接
            Transfer transfer = cluster.getTransfer(null);

            //序列化
            byte[] reqBytes = serializer.serialize(request);

            //发送请求
            byte[] repBytes = transfer.send(reqBytes);

            return serializer.deserialize(repBytes, Response.class);
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
        } catch (SerializeException e) {
            e.printStackTrace();
        } catch (SendDataFailedException e) {
            e.printStackTrace();
        } catch (SocketCloseFailedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
