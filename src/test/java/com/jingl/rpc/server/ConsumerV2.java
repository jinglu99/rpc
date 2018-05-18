package com.jingl.rpc.server;

import com.jingl.rpc.proxy.CglibReferProxy;
import com.jingl.rpc.proxy.Proxy;
import com.jingl.rpc.proxy.RemoteInterface;
import com.jingl.rpc.proxy.TestInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ben on 2018/5/9.
 */
public class ConsumerV2 {
    public static void main(String[] args) throws Exception {
        Proxy proxy = new CglibReferProxy();
        RemoteInterface instance = proxy.getInstance(RemoteInterface.class);
        TestInterface time = proxy.getInstance(TestInterface.class);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = in.readLine();
            if (!input.equals("quit")) {
                try {
                    switch (input) {
                        case "t":
                            System.out.println(time.currentTime());
                            break;
                        default:
                            System.out.println(instance.func(null, null, null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }
}
