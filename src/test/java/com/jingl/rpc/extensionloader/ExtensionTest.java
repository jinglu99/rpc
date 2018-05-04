package com.jingl.rpc.extensionloader;

import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.register.Register;
import com.jingl.rpc.transfer.ReferTransfer;
import com.jingl.rpc.transfer.Transfer;
import org.junit.Test;

/**
 * Created by Ben on 25/03/2018.
 */
public class ExtensionTest {
    @Test
    public void getExtension() {
        Transfer clazz = null;
        clazz = (Transfer) ExtensionLoader.getExtensionLoader(ReferTransfer.class).newInstance();
        System.out.println(clazz.getClass().getName());
    }

    @Test
    public void getExtensionByName() {
        ExtensionLoader loader = ExtensionLoader.getExtensionLoader(Register.class, "zookeeper");
        Register register = (Register) loader.getActiveInstance();
        System.out.println("yes");
    }
}
