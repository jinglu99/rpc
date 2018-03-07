package com.jingl.common.exceptions;

/**
 * Created by Ben on 12/02/2018.
 */
public class SendDataFailedException extends Exception {
    public SendDataFailedException (Throwable cause) {
        super("无法传输数据", cause);
    }
}
