package com.jingl.rpc.extensionloader;

import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.register.Register;
import com.jingl.rpc.exchanger.ReferExchanger;
import com.jingl.rpc.exchanger.Exchanger;
import org.junit.Test;

/**
 * Created by Ben on 25/03/2018.
 */
public class ExtensionTest {
    @Test
    public void getExtension() {
        Exchanger clazz = null;
        clazz = (Exchanger) ExtensionLoader.getExtensionLoader(ReferExchanger.class).newInstance();
        System.out.println(clazz.getClass().getName());
    }

    @Test
    public void getExtensionByName() {
        ExtensionLoader loader = ExtensionLoader.getExtensionLoader(Register.class, "zookeeper");
        Register register = (Register) loader.getActiveInstance();
        System.out.println("yes");
    }
}
