package com.zhaopeng.remote.session.tcp.exchange;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.SessionManager;
import com.zhaopeng.remote.session.listener.SessionListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class ExchangeSessionManager implements SessionManager {

    protected List<SessionListener> sessionListeners = Lists.newArrayList();

    protected Map<String, Session> sessions = new ConcurrentHashMap<>();

    public synchronized void addSessionListeners(SessionListener sessionListener) {
        sessionListeners.add(sessionListener);

    }

    public void addSession(Session session) {

        sessions.putIfAbsent(session.getSessionId(), session);
        log.debug("add session {}", session);
    }

    public synchronized void updateSession(String sessionId) {
        Session session = sessions.get(sessionId);
        session.access();

        sessions.put(sessionId, session);
    }

    public void removeSession(String sessionId) {

        sessions.remove(sessionId);
        log.debug("remove session {}", sessionId);
    }

    public void sessionAccess(String sessionId) {
        Session session = sessions.get(sessionId);
        if (session == null) {
            log.warn("sessionId {} not exits", sessionId);

            return;
        }
        session.access();
    }

    public Collection<Session> listAllSessions() {

        return sessions.values();
    }

    @Override
    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public int getSessionCount() {
        return sessions.size();
    }

}
