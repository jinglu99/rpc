package com.jingl.rpc.server;

import com.jingl.rpc.common.exceptions.ServiceExportFailedException;
import com.jingl.rpc.container.Container;

/**
 * Created by Ben on 26/11/2017.
 */
public class Provider {
    public static void main(String[] args) throws ServiceExportFailedException {

        Container container = new Container();
        container.init();

        while (true);
    }
}
