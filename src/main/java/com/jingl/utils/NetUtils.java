package com.jingl.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Ben on 12/02/2018.
 */
public class NetUtils {
    public static String getIp(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }

    public static void main(String[] args) {
        System.out.println(getIp("localhost"));
    }
}
