package com.jingl.rpc.register.zk;

import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ServiceExportFailedException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.container.Container;
import com.jingl.rpc.register.Register;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

/**
 * Created by Ben on 2018/4/27.
 */
public class ZkTest {
    @Test
    public void zkConnectTest() throws ServiceExportFailedException {
        Container.init();
        Register register = (Register) ExtensionLoader.getExtensionLoader(Register.class, "zookeeper").getActiveInstance();
        register.connect();
        while (true);
    }

}
