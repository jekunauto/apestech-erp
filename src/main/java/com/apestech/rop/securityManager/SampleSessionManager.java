package com.apestech.rop.securityManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.apestech.framework.cache.HazelcastCache;
import org.apache.log4j.Logger;

import com.apestech.oap.session.Session;
import com.apestech.oap.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;


public class SampleSessionManager implements SessionManager {
    protected final Logger logger = Logger.getLogger(getClass());

    private final String CONST_DEFAULT_CACHENAME = "sessionCache";

    @Autowired
    private HazelcastCache cache;

    public void addSession(String sessionId, Session session) {
        cache.put(CONST_DEFAULT_CACHENAME, sessionId, session);
    }

    public Session getSession(String sessionId) {
        return (Session) cache.get(CONST_DEFAULT_CACHENAME, sessionId);
    }

    public void removeSession(String sessionId) {
        cache.remove(CONST_DEFAULT_CACHENAME, sessionId);
    }
}
