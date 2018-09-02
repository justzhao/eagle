package com.zhaopeng.remote.session.listener;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.SessionEvent;
import com.zhaopeng.remote.session.tcp.manager.TcpSessionManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
@Slf4j
public class TcpHeartbeatListener implements Runnable, SessionListener {

    private TcpSessionManager tcpSessionManager = null;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();

    private int checkPeriod = 30 * 1000;
    private volatile boolean stop = false;

    public TcpHeartbeatListener(TcpSessionManager tcpSessionManager) {
        this.tcpSessionManager = tcpSessionManager;
    }

    @Override
    public void onCreated(SessionEvent event) {
        signalQueue();
    }

    @Override
    public void onDestroyed(SessionEvent event) {

    }

    @Override
    public void run() {
        while (!stop) {
            if (isEmpty()) {
                awaitQueue();
            }
            log.info("TcpHeartbeatListener/online session count : " + tcpSessionManager.getSessionCount());
            // sleep period
            try {
                Thread.sleep(checkPeriod);
            } catch (InterruptedException e) {
                log.error("TcpHeartbeatListener run occur InterruptedException!", e);
            }
            // is stop
            if (stop) {
                break;
            }
            // 检测在线用户，多久没有发送心跳，超过规定时间的删除掉
            checkHeartBeat();
        }
    }

    public void checkHeartBeat() {
        Collection<Session> sessions = tcpSessionManager.listAllSessions();
        for (Session session : sessions) {
            if (!session.isValid()) {
                session.close();
                log.info("heart is expire,clear sessionId:" + session.getSessionId());
            }
        }
    }

    private boolean isEmpty() {
        return tcpSessionManager.getSessionCount() == 0;
    }

    private void awaitQueue() {
        boolean flag = lock.tryLock();
        if (flag) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
                log.error("TcpHeartbeatListener awaitQueue occur InterruptedException!", e);
            } catch (Exception e) {
                log.error("await Thread Queue error!", e);
            } finally {
                lock.unlock();
            }
        }
    }

    private void signalQueue() {
        boolean flag = false;
        try {
            flag = lock.tryLock(100, TimeUnit.MILLISECONDS);
            if (flag) { notEmpty.signalAll(); }
        } catch (InterruptedException e) {
            log.error("TcpHeartbeatListener signalQueue occur InterruptedException!", e);
        } catch (Exception e) {
            log.error("signal Thread Queue error!", e);
        } finally {
            if (flag) { lock.unlock(); }
        }
    }

    public void stop() {
        this.stop = true;
    }
}
