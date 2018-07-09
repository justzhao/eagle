package com.zhaopeng.common.bean;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhaopeng on 2018/7/6.
 */
public class Url {

    // 暂时默认为eagle
    private String protocol;


    private String host;

    private int port;

    private String interfaceName;

    // 是消费者还是提供者
    private String type;


    private String str;

    /**
     * 注册地址
     */
    private String registerAddress;


    private Map<String, String> parameters;

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

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getRegisterUrl(){

        return "";
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
}
