package com.zhaopeng.common.bean;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.utils.NetUtils;

import java.net.InetSocketAddress;
import java.util.*;

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


    private int threads;


    /**
     * 注册地址
     */
    private String registerAddress;


    private Map<String, String> parameters;


    private int timeOut;

    private  String path;


    private String full;

    private String ip;

    private String registerUrl;

    /**
     *  某接口对应的provider的url
     */
    private List<String>  urls;

    public Url(){

    }


    public Url(String protocol, String host, int port, String path, Map<String, String> parameters) {

        this.protocol = protocol;

        this.host = host;
        this.port = (port < 0 ? 0 : port);
        // trim the beginning "/"
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        if (parameters == null) {
            parameters = new HashMap<>();
        } else {
            parameters = new HashMap<>(parameters);
        }
        this.parameters = Collections.unmodifiableMap(parameters);
    }


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

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getRegisterUrl() {

        return registerUrl;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }



    public String buildUrlString() {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append("/");
            buf.append(protocol);
            buf.append("/");
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

    private void buildParameters(StringBuilder buf, boolean concat, String[] parameters) {
        if (getParameters() != null && getParameters().size() > 0) {
            List<String> includes = (parameters == null || parameters.length == 0 ? null : Arrays.asList(parameters));
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(getParameters()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0
                        && (includes == null || includes.contains(entry.getKey()))) {
                    if (first) {
                        if (concat) {
                            buf.append("?");
                        }
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



    public String getParameter(String key) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
        }
        return value;
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public String[] getParameter(String key, String[] defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Constants.COMMA_SPLIT_PATTERN.split(value);
    }

    public int getParameter(String key, int defaultValue) {

        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        return i;
    }

    public int getPositiveParameter(String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }


    public static Url valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;

        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?");
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");
            parameters = new HashMap<String, String>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            // case: file:/path/to/file.txt
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }
        i = url.indexOf("@");
        if (i >= 0) {
            url = url.substring(i + 1);
        }
        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if (url.length() > 0) host = url;
        return new Url(protocol, host, port, path, parameters);
    }


    public String toFullString() {
        if (full != null) {
            return full;
        }
        return full = buildString(true);
    }

    private String buildString( boolean appendParameter, String... parameters) {
        return buildString( appendParameter, false, false, parameters);
    }

    private String buildString( boolean appendParameter, boolean useIP, boolean useService, String... parameters) {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }

        String host;
        if (useIP) {
            host = getIp();
        } else {
            host = getHost();
        }
        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        String path;
        if (useService) {
            path = getServiceKey();
        } else {
            path = getPath();
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }
        if (appendParameter) {
            buildParameters(buf, true, parameters);
        }
        return buf.toString();
    }

    public String getPath() {
        return path;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getIp() {
        if (ip == null) {
            ip = NetUtils.getIpByHost(host);
        }
        return ip;
    }

    public String getServiceKey() {
        String inf = getServiceInterface();
        if (inf == null) return null;
        StringBuilder buf = new StringBuilder();
        String group = getParameter(Constants.GROUP_KEY);
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(inf);
        String version = getParameter(Constants.VERSION_KEY);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public String getServiceInterface() {
        return getParameter(Constants.INTERFACE_KEY, path);
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    public boolean hasParameter(String key) {
        String value = getParameter(key);
        return value != null && value.length() > 0;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
