package com.jingl.rpc.common.entity;

import java.io.Serializable;

/**
 * Created by Ben on 26/11/2017.
 */
public class Response implements Serializable {
    private long id;
    private Object response;
    private Exception exception;
    private boolean isSuccess = true;

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

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
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

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", response=" + response +
                ", exception=" + exception +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
