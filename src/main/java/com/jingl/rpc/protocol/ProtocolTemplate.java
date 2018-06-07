package com.jingl.rpc.protocol;

import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.common.exceptions.NotInvokerException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.handle.Invoker;
import com.jingl.rpc.handle.invokers.*;
import com.jingl.rpc.pools.ProviderPool;
import com.jingl.rpc.pools.ExchangerPool;

import java.lang.reflect.Constructor;
import java.util.Stack;

/**
 * Created by Ben on 2018/5/19.
 */
public abstract class ProtocolTemplate {

    private final Cluster cluster = (Cluster) ExtensionLoader.getExtensionLoader(Cluster.class).getActiveInstance();

    private Invoker invoker = null;
    private boolean isSet = false;

    private final InvokerStack beforeSend = new InvokerStack();
    private final InvokerStack afterSend = new InvokerStack();
    private final InvokerStack beforeReturn = new InvokerStack();
    private final InvokerStack afterReturn = new InvokerStack();

    private final InvokerStack beforeMethod = new InvokerStack();
    private final InvokerStack afterMethod = new InvokerStack();
    private final InvokerStack afterResponse = new InvokerStack();

    protected abstract void init() throws NotInvokerException;

    protected InvokerStack beforeSend() {
        return this.beforeSend;
    }

    protected InvokerStack afterSend() {
        return this.afterSend;
    }

    protected InvokerStack beforeReturn() {
        return this.beforeReturn;
    }

    protected InvokerStack afterReturn() {
        return this.afterReturn;
    }

    protected InvokerStack beforeMethod() {
        return this.beforeMethod;
    }

    protected InvokerStack afterMethod() {
        return this.afterMethod;
    }

    protected InvokerStack afterResponse() {
        return this.afterResponse;
    }


    public Invoker getConsumerInvoker() throws FailToGenerateInvokerException {
        try {
            if (!isSet)
                init();

            //Response Invoker
            Invoker beforeReturnInvoker = getInvoker(beforeReturn);
            Invoker afterReturnInvoker = getInvoker(afterReturn);

            Invoker responInvoker = new ResponseInvoker(beforeReturnInvoker, afterReturnInvoker);
            ExchangerPool.setInvoker(responInvoker);
            cluster.init();

            //after Send
            Invoker afterSendInvoker = getInvoker(afterSend);


            Invoker exchangeInvoker = new ExchangeInvoker(afterSendInvoker);

            Invoker serializeInvoker = new SerializeInvoker(exchangeInvoker);

            //before Send
            Invoker beforeSendInvoker = getInvoker(beforeSend, serializeInvoker);


            Invoker futureInvoker = new FutureInvoker(beforeSendInvoker);

            Invoker routerInvoker = new RouterInvoker(futureInvoker);

            FailoverInvoker failoverInvoker = new FailoverInvoker(routerInvoker);

            return failoverInvoker;
        } catch (Exception e) {
            throw new FailToGenerateInvokerException(e);
        }
    }

    public Invoker getProviderInvoker() throws FailToGenerateInvokerException {
        try {
            if (!isSet)
                init();

            Invoker afterResponseInvoker = getInvoker(afterMethod);
            ProviderPool.setAfterResponseInvoker(afterResponseInvoker);

            Invoker beforeMethodInvoker = getInvoker(beforeMethod);

            Invoker afterMethodInvoker = getInvoker(afterMethod);

            Invoker methodInvoker = new MethodInvoker(beforeMethodInvoker, afterMethodInvoker);

            Invoker exceptionInvoker = new ExceptionInvoker(methodInvoker);

            Invoker packageInvoker = new PackageInvoker(exceptionInvoker);

            return packageInvoker;
        } catch (Exception e) {
            throw new FailToGenerateInvokerException(e);
        }
    }

    private Invoker getInvoker(InvokerStack stack) throws FailToGenerateInvokerException {
        return getInvoker(stack, null);
    }

    private Invoker getInvoker(InvokerStack stack, Invoker next) throws FailToGenerateInvokerException {
        try {
            Invoker invoker = next;
            while (stack.size() > 0) {
                Class clazz = stack.pop();
                if (clazz == null) continue;
                Constructor<Invoker> constructor = clazz.getConstructor(Invoker.class);
                invoker = constructor.newInstance(invoker);
            }
            return invoker;
        }catch (Exception e) {
            throw new FailToGenerateInvokerException(e);
        }
    }

    public class InvokerStack {
        private Stack<Class> stack = new Stack<>();

        public InvokerStack add(Class clazz) throws NotInvokerException {
            if (!Invoker.class.isAssignableFrom(clazz)) {
                throw new NotInvokerException(clazz);
            }
            stack.push(clazz);
            return this;
        }

        public Class pop() {
            return stack.pop();
        }

        public int size() {
            return stack.size();
        }
    }
}
