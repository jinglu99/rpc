package com.jingl.rpc.exchanger.netty;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Ben on 31/03/2018.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    public NettyDecoder() {
        super(Integer.MAX_VALUE, 0, 4, 0, 4);
    }
}
