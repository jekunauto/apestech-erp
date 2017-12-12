package com.apestech.rop.securityManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.apestech.oap.session.Session;
import com.apestech.oap.session.SessionManager;


public class SampleSessionManager implements SessionManager {
	protected final Logger logger = Logger.getLogger(getClass());

	private final Map<String, Session> sessionCache = new ConcurrentHashMap<String, Session>(128, 0.75f, 32);

	public void addSession(String sessionId, Session session) {
		sessionCache.put(sessionId, session);
	}

	public Session getSession(String sessionId) {
		return sessionCache.get(sessionId);
	}

	public void removeSession(String sessionId) {
		sessionCache.remove(sessionId);
	}
}
