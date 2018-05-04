package com.jingl.rpc.common.exceptions;

/**
 * Created by Ben on 25/03/2018.
 */
public class NotInterfaceExcetption extends RuntimeException {
    public NotInterfaceExcetption() {
        super("ExtensionLoader only initialize Interface");
    }
}
