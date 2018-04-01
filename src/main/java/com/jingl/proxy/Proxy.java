package com.jingl.proxy;

import com.jingl.handle.Invoker;

/**
 * Created by Ben on 13/02/2018.
 */
public interface Proxy {
    <T> T getInstance(Class T, Invoker invoker);
}
