package com.jingl.rpc.server;

import com.jingl.rpc.common.exceptions.ServiceExportFailedException;
import com.jingl.rpc.container.Container;

/**
 * Created by Ben on 26/11/2017.
 */
public class Provider {
    public static void main(String[] args) throws ServiceExportFailedException {

        Container container = new Container();
//        RemoteInterface instance = new RemoteInterface() {
//            @Override
//            public String func(Object var1, Object var2, Object var3) {
//                System.out.println("func(var1, var2, var3) called");
//                return "called success";
//            }
//        };
//        container.setInstance(RemoteInterface.class, instance);

//        Handler handler = new ProviderHandle();
//        Transfer transfer = new SocketExportTransfer(2532, handler);
//        transfer.export();
        container.init();

        while (true);
    }
}
