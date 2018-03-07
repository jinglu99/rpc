package com.jingl.container;

import com.jingl.common.exceptions.ServiceExportFailedException;
import com.jingl.handle.Handler;
import com.jingl.handle.ProviderHandle;
import com.jingl.transfer.SocketExportTransfer;
import com.jingl.transfer.Transfer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 13/12/2017.
 */
public class Container {
    private static ConcurrentHashMap<Class, Object> container = new ConcurrentHashMap();

    public Object getInstance(Class clazz) {
        return container.get(clazz);
    }

    public void setInstance(Class clazz, Object obj) {
        container.put(clazz, obj);
    }

    public int init() throws ServiceExportFailedException {
        Handler handler = new ProviderHandle();
        Transfer transfer = new SocketExportTransfer(2532, handler);
        transfer.export();
        return 0;
    }
}
