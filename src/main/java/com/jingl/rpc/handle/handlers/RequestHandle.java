//package com.jingl.rpc.handle.handlers;
//
//import com.jingl.rpc.cluster.Cluster;
//import com.jingl.rpc.common.entity.Directory;
//import com.jingl.rpc.common.entity.URL;
//import com.jingl.rpc.common.entity.Request;
//import com.jingl.rpc.common.exceptions.ConnectionFailedException;
//import com.jingl.rpc.common.exceptions.NoProviderFoundException;
//import com.jingl.rpc.common.exceptions.SerializeException;
//import com.jingl.rpc.common.extension.ExtensionLoader;
//import com.jingl.rpc.handle.Handler;
//import com.jingl.rpc.handle.Invoker;
//import com.jingl.rpc.serializer.FastjsonRPCSerializer;
//import com.jingl.rpc.serializer.RPCSerializer;
//import com.jingl.rpc.exchanger.ReferTransfer;
//import com.jingl.rpc.exchanger.Exchanger;
//
///**
// * Created by Ben on 26/11/2017.
// */
//public class RequestHandle implements Handler {
//
//    private Cluster cluster = new Cluster() {
//        @Override
//        public void init() {
//
//        }
//
//        @Override
//        public Exchanger getExchanger(Class clazz) throws ConnectionFailedException {
//            ReferExchanger referTransfer = (ReferExchanger) ExtensionLoader.getExtensionLoader(ReferExchanger.class).newInstance();
//            referTransfer.setParams(new URL(null, "localhost", 2532, null, null), null);
//            return referTransfer;
//        }
//
//        @Override
//        public Directory getDirectory(Class clazz) {
//            return null;
//        }
//
//        @Override
//        public void connect(Invoker invoker) throws ConnectionFailedException {
//
//        }
//
//    };
//
//    private RPCSerializer serializer = new FastjsonRPCSerializer();
//
//    @Override
//    public Object invoke(Object val) {
//
//        Request request = (Request) val;
//
//        try {
//            //获得一个连接
//            Exchanger exchanger = cluster.getExchanger(null);
//
//            //序列化
//            byte[] reqBytes = serializer.serialize(request);
//
//            //发送请求
////            Response repBytes = exchanger.send(request);
//
//            return null;
//        } catch (ConnectionFailedException e) {
//            e.printStackTrace();
//        } catch (SerializeException e) {
//            e.printStackTrace();
////        } catch (SendDataFailedException e) {
////            e.printStackTrace();
////        } catch (SocketCloseFailedException e) {
////            e.printStackTrace();
//        } catch (NoProviderFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
