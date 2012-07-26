package com.coirius.FatCow;

import java.util.Random;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class ServerSession {
	private String _sessionKey = "";
	private String _sessionHash = "";
	private Socket _socket = null;
	private boolean _sessionStatus = false;

	public ServerSession(Socket socket) throws ServerSessionException {
		try {
			Random rnd = new Random();
			for(int i = 0; i < 32; i++)
				_sessionKey += (char) (rnd.nextInt(126 - 33) + 33);
			//_sessionHash = ;
			_socket = socket;
		} catch (Exception ex) {
			throw new ServerSessionException(ex.getMessage());
		}
	}

	public InputStream getInputStream() throws IOException {
		return _socket.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return _socket.getOutputStream();
	}

	public void close() throws IOException {
		_socket.close();
	}

	public boolean check(String hash) {
		_sessionStatus = _sessionHash.equalsIgnoreCase(hash);
		return _sessionStatus;
	}

	public String getAuthKey() {
		return _sessionKey;
	}

	public boolean getAuthStatus() {
		return _sessionStatus;
	}
}
