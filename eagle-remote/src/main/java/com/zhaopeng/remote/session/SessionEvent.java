package com.zhaopeng.remote.session;

import java.util.EventObject;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
public class SessionEvent extends EventObject {


    public SessionEvent(Object source) {
        super(source);
    }

    public Session getSession() {
        return (Session) super.getSource();
    }
}
