package com.jingl.rpc.server.service;

import com.jingl.rpc.common.annotation.Provider;

import java.util.Date;

/**
 * Created by Ben on 2018/5/10.
 */
@Provider
public class TestClass implements TestInterface {
    @Override
    public String currentTime() {
        Date date = new Date();
        return date.toString();
    }

    @Override
    public byte[] get100bytes() {
        return getDataByLength(100);
    }

    public byte[] get1kb() {
        return getDataByLength(1024);
    }

    public byte[] get10kb() {
        return getDataByLength(1024 * 10);
    }

    public byte[] get1Mb() {
        return getDataByLength(1024 * 1024);
    }

    private byte[] getDataByLength(int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i< length; i++) {
            bytes[i] = (byte) (Math.random()*128);
        }
        return bytes;
    }
}
