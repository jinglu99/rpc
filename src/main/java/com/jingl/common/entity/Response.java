package com.jingl.common.entity;

import java.io.Serializable;

/**
 * Created by Ben on 26/11/2017.
 */
public class Response implements Serializable {
    long id;
    Object response;

    public Response() {
    }

    public Response(long id, Object response) {
        this.id = id;
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                '}';
    }
}
