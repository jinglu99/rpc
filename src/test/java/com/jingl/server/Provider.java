package com.jingl.server;

import com.apple.eawt.AppEvent;
import com.jingl.container.Container;
import com.jingl.proxy.Data;
import com.jingl.proxy.RemoteInterface;

/**
 * Created by Ben on 26/11/2017.
 */
public class Provider {
    public static void main(String[] args) {

        Container container = new Container();
        RemoteInterface instance = new RemoteInterface() {
            @Override
            public String func(Object var1, Object var2, Object var3) {
                System.out.println("func(var1, var2, var3) called");
                return "called success";
            }

            @Override
            public void func(Data data) {
                System.out.println("func(data) called");
            }
        };
        container.setInstance(RemoteInterface.class, instance);

        System.out.println("provider start");
        new ProviderServer().init();
    }
}
