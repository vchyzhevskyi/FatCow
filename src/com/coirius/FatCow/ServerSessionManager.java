package com.coirius.FatCow;

import java.util.ArrayList;
import java.net.Socket;
import java.io.IOException;

public class ServerSessionManager {
	private static final ServerSessionManager _sessionManager = new ServerSessionManager();
	private ArrayList<ServerSession> _sessionList = null;

	public static ServerSessionManager getInstance() {
		return _sessionManager;
	}

	private ServerSessionManager() {
		_sessionList = new ArrayList<ServerSession>();
	}

	public int start(Socket socket) throws ServerSessionException {
		ServerSession session = new ServerSession(socket);
		_sessionList.add(session);
		return _sessionList.indexOf(session);
	}

	public void stop(int id) throws IOException {
		_sessionList.get(id).close();
		_sessionList.remove(id);
	}

	public ServerSession getSession(int id) {
		return _sessionList.get(id);
	}
}
