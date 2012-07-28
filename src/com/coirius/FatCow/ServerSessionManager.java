package com.coirius.FatCow;

import java.util.HashMap;
import java.net.Socket;
import java.io.IOException;

public class ServerSessionManager {
	private static final ServerSessionManager _sessionManager = new ServerSessionManager();
	private HashMap<String, ServerSession> _sessionList = null;

	public static ServerSessionManager getInstance() {
		return _sessionManager;
	}

	private ServerSessionManager() {
		_sessionList = new HashMap<String, ServerSession>();
	}

	public String start(Socket socket) throws ServerSessionException {
		ServerSession session = new ServerSession(socket);
		if(session.getSessionKey().isEmpty())
			throw new ServerSessionException("Session key is empty.");
		else if(_sessionList.containsKey(session.getSessionKey()))
			throw new ServerSessionException("Session with key " + session.getSessionKey() + " already exists.");
		_sessionList.put(session.getSessionKey(), session);
		return session.getSessionKey();
	}

	public void stop(String sessionKey) throws IOException, ServerSessionException {
		if(!_sessionList.containsKey(sessionKey))
			throw new ServerSessionException("Session does not exists.");
		_sessionList.get(sessionKey).close();
		_sessionList.remove(sessionKey);
	}

	public ServerSession getSession(String sessionKey) {
		return _sessionList.get(sessionKey);
	}
}
