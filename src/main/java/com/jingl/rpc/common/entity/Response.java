package com.jingl.rpc.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben on 26/11/2017.
 */
public class Response implements Serializable {
    private long id;
    private Object response;
    private Throwable exception;
    private boolean isSuccess = true;
    private Map values = null;


    public Response() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
        if (exception != null) {
            isSuccess = false;
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Map getValues() {
        return values;
    }

    public void setValues(Map values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", response=" + response +
                ", exception=" + exception +
                ", isSuccess=" + isSuccess +
                ", values=" + values +
                '}';
    }
}
