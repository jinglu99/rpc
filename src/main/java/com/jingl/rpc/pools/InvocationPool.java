package com.jingl.rpc.pools;

import com.jingl.rpc.common.entity.Invocation;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ben on 01/04/2018.
 */
public class InvocationPool {
    private static final ConcurrentHashMap<Long, Invocation> responses = new ConcurrentHashMap<>();

    public static Invocation getInvocation(long id) {
        return responses.get(id);
    }

    public static void addInvocation(Invocation invocation) {
        long id = invocation.getId();
        responses.putIfAbsent(id, invocation);
    }

    public static void removeInvoceation(long id) {
        responses.remove(id);
    }
}
