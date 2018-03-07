package com.jingl.common.entity;

import java.io.Serializable;

/**
 * Created by Ben on 26/11/2017.
 */
public class Response implements Serializable {
    Object response;

    public Response(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                '}';
    }
}
