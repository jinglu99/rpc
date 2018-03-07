package com.jingl.server;

import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.container.Container;
import com.jingl.handle.Handler;
import com.jingl.handle.ProviderHandle;
import com.jingl.proxy.RemoteInterface;
import com.jingl.transfer.SocketExportTransfer;
import com.jingl.transfer.Transfer;

/**
 * Created by Ben on 26/11/2017.
 */
public class Provider {
    public static void main(String[] args) throws ServiceExportFailedException {

        Container container = new Container();
        RemoteInterface instance = new RemoteInterface() {
            @Override
            public String func(Object var1, Object var2, Object var3) {
                System.out.println("func(var1, var2, var3) called");
                return "called success";
            }
        };
        container.setInstance(RemoteInterface.class, instance);

        Handler handler = new ProviderHandle();
        Transfer transfer = new SocketExportTransfer(2532, handler);
        transfer.export();

        System.out.println("provider start");
        while (true);
    }
}
