package com.zhaopeng.remote.session;

import com.zhaopeng.remote.session.listener.SessionListener;

public interface Session {

    /**
     * @return
     */
    String getSessionId();

    /**
     * session access
     */
    void access();

    /**
     *  session is valid
    */
     boolean isValid();

    /**
     * connect to sever
     */
    void connect();

    /**
     * colse this session
     */
    void close();

    /**
     * release all resoures
     */
    void recycle();

    /**
     * add Attr to session
     * @param name
     * @param obj
     */
    void addAttr(String name,Object obj);


    /**
     * @param connection
     */
    void setConnection(Connection connection);

    Connection getConnection();

    /**
     * @param sessionId
     */
    void setSessionId(String sessionId);



    /**
     *
     * @param listener
     */
    void addSessionListener(SessionListener listener);
}
