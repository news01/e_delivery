package com.thinkgem.jeesite.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener{

	 private SessionUtil context = SessionUtil.getInstance();
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		context.addSession(sessionEvent.getSession());
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		HttpSession session = sessionEvent.getSession();
        context.delSession(session);
	}

}
