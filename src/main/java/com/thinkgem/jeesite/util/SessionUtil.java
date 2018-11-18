package com.thinkgem.jeesite.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.session.mgt.SessionContext;

public class SessionUtil {
	private static SessionUtil instance;
	private Map<String, HttpSession> sessionMap;
	private SessionUtil(){
        sessionMap = new HashMap<String, HttpSession>();
    }
	public static SessionUtil getInstance(){
        if(instance == null){
            instance = new SessionUtil();
        }
        return instance;
    }
	public synchronized void addSession(HttpSession session){
        if(session != null){
            sessionMap.put(session.getId(), session);
        }
    }
	public synchronized void delSession(HttpSession session){
        if(session != null){
            sessionMap.remove(session.getId());
        }
    }
	public synchronized HttpSession getSession(String sessionId){
        if(sessionId == null)
            return null;
        return sessionMap.get(sessionId);
    }
}
