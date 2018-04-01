package com.jingl.handle.handlers;

import com.jingl.cluster.Cluster;
import com.jingl.common.entity.URL;
import com.jingl.common.entity.Request;
import com.jingl.common.exceptions.ConnectionFailedException;
import com.jingl.common.exceptions.SerializeException;
import com.jingl.common.extension.ExtensionLoader;
import com.jingl.handle.Handler;
import com.jingl.serializer.FastjsonRPCSerializer;
import com.jingl.serializer.RPCSerializer;
import com.jingl.transfer.ReferTransfer;
import com.jingl.transfer.Transfer;

/**
 * Created by Ben on 26/11/2017.
 */
public class RequestHandle implements Handler {

    private Cluster cluster = new Cluster() {
        @Override
        public Transfer getTransfer(Class clazz) throws ConnectionFailedException {
            ReferTransfer referTransfer = (ReferTransfer) ExtensionLoader.getExtensionLoder(ReferTransfer.class).newInstance();
            referTransfer.setParams(new URL(null, "localhost", 2532, null, null), null);
            return referTransfer;
        }
    };

    private RPCSerializer serializer = new FastjsonRPCSerializer();

    @Override
    public Object invoke(Object val) {

        Request request = (Request) val;

        try {
            //获得一个连接
            Transfer transfer = cluster.getTransfer(null);

            //序列化
            byte[] reqBytes = serializer.serialize(request);

            //发送请求
//            Response repBytes = transfer.send(request);

            return null;
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
        } catch (SerializeException e) {
            e.printStackTrace();
//        } catch (SendDataFailedException e) {
//            e.printStackTrace();
//        } catch (SocketCloseFailedException e) {
//            e.printStackTrace();
        }
        return null;
    }
}