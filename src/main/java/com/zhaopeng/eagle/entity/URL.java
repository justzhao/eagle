package com.zhaopeng.eagle.entity;

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

    public URL() {
    }


    public URL(String protocol, String host, int port, String interfaceName, String type) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.interfaceName = interfaceName;
        this.type = type;
    }

    private String buildUrlString() throws Exception {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append("/");
            buf.append(protocol);
            buf.append("/");
        }
        if(interfaceName==null){

            throw new Exception("interfaceName is null");
        }
        buf.append(interfaceName);
        buf.append("/");
        buf.append(type);
        buf.append("/");

        String host = getHost();

        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        return buf.toString();
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
}
