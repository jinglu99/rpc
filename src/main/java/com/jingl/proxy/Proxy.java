package com.jingl.proxy;

/**
 * Created by Ben on 13/02/2018.
 */
public interface Proxy {
    <T> T getInstance(Class T);
}
