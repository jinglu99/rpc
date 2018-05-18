package com.jingl.rpc.common.entity;

import com.google.common.collect.Lists;
import com.jingl.rpc.utils.NetUtils;
import org.apache.commons.lang3.StringUtils;

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
    private final String interfaceName;
    private final Map<String, Object> params = new HashMap();

    private volatile transient String ip;

    public URL(String protocol, String host, int port, String interfaceName, Map params) {
        this.protocol = protocol;
        this.host = host;
        this.interfaceName = interfaceName;
        this.port = port;
    }

    public URL setProtocol(String protocol) {
        return new URL(protocol, host, port, interfaceName, params);
    }

    public String getProtocol() {
        return protocol;
    }

    public URL setHost(String host) {
        return new URL(protocol, host, port, interfaceName, params);
    }

    public String getHost() {
        return host;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public URL setPort(int port) {
        return new URL(protocol, host, port, interfaceName, params);
    }

    public int getPort() {
        return port;
    }

    public URL setParams(Map params) {
        return new URL(protocol, host, port, interfaceName, params);
    }

    public Map getParams() {
        return params;
    }

    public URL addParamter(String key, Object obj) {
        if (StringUtils.isBlank(key)) {
            return this;
        }
        Map newMap = new HashMap(params);
        newMap.put(key, obj);
        return new URL(protocol, host, port, interfaceName, params);
    }

    public Object getParamter(String key) {
        return params.get(key);
    }

    public URL removeParamter(String key) {
        Map newMap = new HashMap(params);
        newMap.remove(key);
        return new URL(protocol, host, port, interfaceName, params);
    }

    public URL clearParameters() {
        return new URL(protocol, host, port, interfaceName, new HashMap<String, String>());
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

        if (StringUtils.isNotBlank(interfaceName)) {
            if (!interfaceName.startsWith("/")) {
                buf.append("/");
            }
            buf.append(interfaceName);
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

    @Override
    public String toString() {
        return "URL{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", interfaceName='" + interfaceName + '\'' +
                ", params=" + params +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URL url = (URL) o;

        if (port != url.port) return false;
        if (!StringUtils.equals(protocol, url.protocol)) return false;
        if (!StringUtils.equals(host, url.host)) return false;
        return StringUtils.equals(interfaceName, url.interfaceName);
    }

    @Override
    public int hashCode() {
        int result = protocol != null ? protocol.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (interfaceName != null ? interfaceName.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        URL url1 = new URL("rpc","127.0.0.1",2532, null, null);
        URL url2 = new URL("rpc","127.0.0.1",2532, null, null);

        System.out.println(url1.hashCode());
        System.out.println(url2.hashCode());
        System.out.println(url1.equals(url2));

    }
}
