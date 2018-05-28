package com.jingl.rpc.cluster.strategy;

import com.jingl.rpc.cluster.LoadBalance;
import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 2018/5/21.
 */
public class RandomLoadBalance implements LoadBalance{

    @Override
    public Object select(Invocation invocation, Set urls) {
        List<URL> tmp = new ArrayList<>(urls);
        if (tmp.size() > 0) {
            int index = (int) (Math.random() * tmp.size());
            return tmp.get(index);
        }
        return null;
    }
}
