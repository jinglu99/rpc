package com.jingl.rpc.handle.invokers;

import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.entity.Invocation;
import com.jingl.rpc.common.entity.Response;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.ConnectionFailedException;
import com.jingl.rpc.common.exceptions.InvokerException;
import com.jingl.rpc.common.exceptions.NoProviderFoundException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.transfer.Transfer;
import com.jingl.rpc.utils.PropertyUtils;
import org.apache.log4j.Logger;

/**
 * 重试逻辑
 * Created by Ben on 2018/4/23.
 */
public class FailoverInvoker implements Invoker {
    private final Logger logger = Logger.getLogger(FailoverInvoker.class);
    private final Invoker invoker;

    private final int retryTimes = Integer.valueOf(Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_CONSUMER_RETRY)) <= 0 ? Constants.DEFAULT_PROPERTY_CONSUMER_RETRY:PropertyUtils.getProperty(Constants.PROPERTY_CONSUMER_RETRY));

    public FailoverInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object val) throws InvokerException {
        Invocation invocation = (Invocation) val;
        Response response = null;
        InvokerException exception = null;

        if (invocation.isInterupt())
            return null;

        for (int i = 0; i < retryTimes; i++) {
            try {
                response = (Response) invoker.invoke(invocation);
            } catch (InvokerException e) {  //当invoke链中抛出异常时进行重试
                logger.error("The No." + i + "try failed", e.getCause());
                exception = e;
                continue;
            }
            if (response.isSuccess()) {
                return response.getResponse();  //如果调用成功返回结果
            } else {
                throw new InvokerException(response.getException()); //provider抛出异常，不进行重试。
            }
        }
        throw exception;
    }
}
