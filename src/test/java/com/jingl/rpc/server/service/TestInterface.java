package com.jingl.rpc.server.service;

/**
 * Created by Ben on 2018/5/10.
 */
public interface TestInterface {
    String currentTime();

    byte[] get100bytes();

    byte[] get1kb();

    byte[] get10kb();

    byte[] get1Mb();
}
