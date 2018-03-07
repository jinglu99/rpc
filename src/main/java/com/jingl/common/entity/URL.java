package com.jingl.common.entity;

import com.google.common.collect.Lists;
import com.jingl.utils.NetUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ben on 11/02/2018.
 */
public final class URL {
    private final String protocol;
    private final String host;
    private final int port;
    private final String path;
    private final Map<String, Object> params = new HashedMap();

    private volatile transient String ip;

    public URL() {
        this.protocol = null;
        this.host = null;
        this.port = -1;
        this.path = null;
    }

    public URL(String protocol, String host, int port, String path, Map params) {
        this.protocol = protocol;
        this.host = host;
        this.path = path;
        this.port = port;
    }

    public URL setProtocol(String protocol) {
        return new URL(protocol, host, port, path, params);
    }

    public String getProtocol() {
        return protocol;
    }

    public URL setHost(String host) {
        return new URL(protocol, host, port, path, params);
    }

    public String getHost() {
        return host;
    }

    public URL setPath(String path) {
        return new URL(protocol, host, port, path, params);
    }

    public String getPath() {
        return path;
    }

    public URL setPort(int port) {
        return new URL(protocol, host, port, path, params);
    }

    public int getPort() {
        return port;
    }

    public URL setParams(Map params) {
        return new URL(protocol, host, port, path, params);
    }

    public Map getParams() {
        return params;
    }

    public URL addParamter(String key, Object obj) {
        if (StringUtils.isBlank(key)) {
            return this;
        }
        Map newMap = new HashedMap(params);
        newMap.put(key, obj);
        return new URL(protocol, host, port, path, params);
    }

    public Object getParamter(String key) {
        return params.get(key);
    }

    public URL removeParamter(String key) {
        Map newMap = new HashedMap(params);
        newMap.remove(key);
        return new URL(protocol, host, port, path, params);
    }

    public URL clearParameters() {
        return new URL(protocol, host, port, path, new HashMap<String, String>());
    }

    public String buildString() {
        StringBuilder buf = new StringBuilder();
        if (StringUtils.isNotBlank(protocol)) {
            buf.append(protocol);
            buf.append("//");
        }

        if (StringUtils.isNotBlank(host)) {
            buf.append(host);
            buf.append(":");
            buf.append(port);
        }

        if (StringUtils.isNotBlank(path)) {
            if (!path.startsWith("/")) {
                buf.append("/");
            }
            buf.append(path);
        }

        if (params.size()>0) {
            buf.append("?");
            buf.append(buildPatamters());
        }

        return buf.toString();
    }

    public String buildPatamters() {
        List paramsList = Lists.newArrayList();
        for (String key : params.keySet()) {
            Object object = params.get(key);
            if (object == null) continue;

            StringBuilder buf = new StringBuilder();
            buf.append(key);
            buf.append("=");
            buf.append(object.toString());
            paramsList.add(buf.toString());
        }

        return StringUtils.join(paramsList,"&");
    }

    public String getIp() {
        if (StringUtils.isBlank(this.ip)) {
            this.ip = NetUtils.getIp(host);
        }

        return this.ip;
    }


}
