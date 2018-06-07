package com.jingl.rpc.netty.sample;

/**
 * Created by Ben on 29/03/2018.
 */
public class Message {
    public Message(int size, byte[] body) {
        this.size = size;
        this.body = body;
    }

    private int size;
    private byte[] body;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
