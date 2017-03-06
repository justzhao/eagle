package com.zhaopeng.eagle.entity;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 用来分装dubbo服务消费者或者提供者的方法url参数
 * Created by zhaopeng on 2017/2/20.
 */
public class URL {

    // 暂时默认为eagle
    private String protocol;


    private String host;

    private int port;

    private String interfaceName;

    // 是消费者还是提供者
    private String type;


    private String str;


    private Map<String, String> parameters;

    /**
     * 存放provider的urls
     */
    private List<String> urls;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public URL() {
        this.protocol = null;

        this.host = null;
        this.port = 0;

        this.parameters = null;
    }


    public URL(String protocol, String host, int port, String interfaceName, String type) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.interfaceName = interfaceName;
        this.type = type;
    }

    public URL(String protocol, String host, int port, String interfaceName, String type, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.interfaceName = interfaceName;
        this.type = type;
        this.parameters = parameters;

    }

    private String buildUrlString() throws Exception {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append("/");
            buf.append(protocol);
            buf.append("/");
        }
        if (interfaceName == null) {
            throw new Exception("interfaceName is null");
        }
        buf.append(interfaceName);
        buf.append("/");
        buf.append(type);
        String host = getHost();
        if (host != null && host.length() > 0) {
            buf.append("/");
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        buildParameters(buf);
        return buf.toString();
    }

    private void buildParameters(StringBuilder buf) {
        if (getParameters() != null && getParameters().size() > 0) {
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<>(getParameters()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0) {
                    if (first) {
                        buf.append("?");
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
    }

    @Override
    public String toString() {
        if (str == null) {
            try {
                str = buildUrlString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public String getParameter(String key, String defaultValue) {
        String value = parameters.get(key);
        return value != null ? value : defaultValue;
    }


    public int getParameter(String key, int defaultValue) {
        String value = parameters.get(key);
        return value != null ? Integer.valueOf(value).intValue() : defaultValue;
    }


    public String getPath() throws Exception {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append("/");
            buf.append(protocol);
            buf.append("/");
        }
        if (interfaceName == null) {

            throw new Exception("interfaceName is null");
        }
        buf.append(interfaceName);
        buf.append("/");
        buf.append(type);
        return buf.toString();
    }

    public String getProviderUrl() {

        if (urls == null || urls.isEmpty()) return null;

        int size=urls.size();

        int index = (int) Math.round(Math.random()*(size-1));


        return  urls.get(index);


    }


}
