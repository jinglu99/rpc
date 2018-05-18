package com.jingl.rpc.server.service;

import com.jingl.rpc.common.annotation.Provider;
import com.jingl.rpc.proxy.TestInterface;

import java.text.SimpleDateFormat;
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
}
