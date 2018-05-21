package com.jingl.rpc.exchanger;

import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.DeadProviderException;
import com.jingl.rpc.common.exceptions.NoAvailableConnectionException;
import com.jingl.rpc.handle.invokers.ResponseInvoker;
import com.jingl.rpc.pools.ExchangerPool;
import org.junit.Test;

/**
 * Created by Ben on 2018/5/8.
 */
public class TransferPoolTest {
    @Test
    public void testTransferPoolConnect() throws InterruptedException {
        URL url = new URL("rpc", "127.0.0.1", 2532, null, null);
        ExchangerPool.connect(url, new ResponseInvoker(null,null));
        Exchanger exchanger = null;
        try {
            exchanger = ExchangerPool.getTransfer(url);
        } catch (NoAvailableConnectionException e) {
            e.printStackTrace();
        } catch (DeadProviderException e) {
            e.printStackTrace();
        }
//        Thread.sleep(5000);
//        exchanger.close();

        while (true) {
            Thread.sleep(1000);
            System.out.println(exchanger.isActive());
        }
    }
}
