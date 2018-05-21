package com.jingl.rpc.common.entity;

import com.jingl.rpc.exchanger.Exchanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Ben on 01/04/2018.
 */
public class Invocation {

    private final long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

    private volatile Class clazz;

    private final Request request = new Request();

    private volatile byte[] serializedRequest;

    private final BlockingQueue<Response> queue = new ArrayBlockingQueue<Response>(1);

    private volatile Response response;

    private volatile Exception exception;

    private volatile boolean interupt = false;

    private volatile boolean isException = false;

    private volatile Exchanger exchanger;

    private volatile URL providerURL;

    private final List<URL> triedURL = new ArrayList<>();

    public Invocation() {
        request.setId(id);
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        if (this.clazz == null) {
            synchronized (this) {
                if (this.clazz == null)
                    this.clazz = clazz;
            }
        }
    }

    public long getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public byte[] getSerializedRequest() {
        return serializedRequest;
    }

    public void setSerializedRequest(byte[] serializedRequest) {
        this.serializedRequest = serializedRequest;
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isInterupt() {
        return interupt;
    }

    public void setInterupt(boolean interupt) {
        this.interupt = interupt;
    }

    public boolean isException() {
        return isException;
    }

    public void setException(boolean exception) {
        isException = exception;
    }

    public URL getProviderURL() {
        return providerURL;
    }

    public void setProviderURL(URL providerURL) {
        this.providerURL = providerURL;
    }

    public List<URL> getTriedURL() {
        return triedURL;
    }

    public BlockingQueue<Response> getQueue() {
        return queue;
    }


    public Exchanger getExchanger() {
        return exchanger;
    }

    public void setExchanger(Exchanger exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "id=" + id +
                ", request=" + request +
                ", serializedRequest=" + Arrays.toString(serializedRequest) +
                ", response=" + response +
                ", exception=" + exception +
                ", interupt=" + interupt +
                ", isException=" + isException +
                ", providerURL=" + providerURL +
                ", triedURL=" + triedURL +
                '}';
    }
}
