package com.zhaopeng.remote.session;

import java.util.Collection;

public interface SessionManager {

    /**
     * add session
     */
    void addSession(Session session);

    /**
     * remove session
     */
    void removeSession(String sessionId);

    /**
     * get all session
     *
     * @return
     */
    Collection<Session> listAllSessions();

    /**
     * 获取一个session
     *
     * @param sessionId
     * @return
     */
    Session getSession(String sessionId);

    /**
     * accsss session
     *
     * @param sessionId
     */
    void sessionAccess(String sessionId);

    /**
     * get count
     *
     * @return
     */
    int getSessionCount();

    /**
     * update
     *
     * @param sessionId
     */
    void updateSession(String sessionId);

}
