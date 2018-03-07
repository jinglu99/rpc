package com.jingl.utils;

import java.util.Optional;

/**
 * Created by Ben on 13/02/2018.
 */
public class ArrayUtils {
    /**
     * 合并两个字节数组
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] concat(byte[] a, byte[] b) {
        a = Optional.ofNullable(a).orElse(new byte[0]);
        b = Optional.ofNullable(b).orElse(new byte[0]);

        byte[] newArray = new byte[a.length + b.length];
        System.arraycopy(a, 0, newArray, 0, a.length);
        System.arraycopy(b, 0, newArray, a.length, b.length);
        return newArray;
    }
}
