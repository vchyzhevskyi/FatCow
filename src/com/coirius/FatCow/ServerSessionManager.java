/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ServerSessionManager {
	private static final ServerSessionManager _sessionManager = new ServerSessionManager();

	public static ServerSessionManager getInstance() {
		return _sessionManager;
	}

	private HashMap<String, ServerSession> _sessionList = null;

	private ServerSessionManager() {
		_sessionList = new HashMap<String, ServerSession>();
	}

	public ServerSession getSession(String sessionKey) {
		return _sessionList.get(sessionKey);
	}

	public String start(Socket socket) throws ServerSessionException,
			IOException {
		ServerSession session = new ServerSession(socket);
		if (session.getSessionKey().isEmpty()) {
			session.close();
			throw new ServerSessionException("Session key is empty.");
		} else if (_sessionList.containsKey(session.getSessionKey())) {
			session.close();
			throw new ServerSessionException("Session with key "
					+ session.getSessionKey() + " already exists.");
		}
		_sessionList.put(session.getSessionKey(), session);
		return session.getSessionKey();
	}

	public void stop(String sessionKey) throws IOException,
			ServerSessionException {
		if (!_sessionList.containsKey(sessionKey))
			throw new ServerSessionException("Session does not exists.");
		_sessionList.get(sessionKey).close();
		_sessionList.remove(sessionKey);
	}
}
