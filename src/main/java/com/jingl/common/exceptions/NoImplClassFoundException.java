package com.jingl.common.exceptions;

/**
 * Created by Ben on 25/03/2018.
 */
public class NoImplClassFoundException extends RuntimeException {
    public NoImplClassFoundException() {
        super("No implement class found!");
    }
}