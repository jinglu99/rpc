package com.jingl.transfer;

import com.jingl.common.entity.URL;
import com.jingl.common.exceptions.ConnectionFailedException;
import com.jingl.common.exceptions.SendDataFailedException;
import com.jingl.common.exceptions.SocketCloseFailedException;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Ben on 12/02/2018.
 */
public class SocketReferTransferTest {

    /**
     * 发送测试
     */
    @Test
    public void send() throws ConnectionFailedException, SendDataFailedException, SocketCloseFailedException, IOException {
        URL url = new URL(null, "localhost", 1234, null, null);
        Transfer transfer = new SocketReferTransfer(url);
        String text = "hello world!";
        System.out.println("send : " + text);
        byte[] rep = transfer.send(text.getBytes());
        System.out.println("receive : " + new String(rep));
    }
}
