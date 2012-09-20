/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.coirius.FatCow.Utils.HashUtils;

public class ServerSession implements Closeable {
	private String _sessionHash = "";
	private String _sessionKey = "";
	private boolean _sessionStatus = false;
	private Socket _socket = null;

	public ServerSession(Socket socket) throws ServerSessionException {
		try {
			Random rnd = new Random();
			for (int i = 0; i < 32; i++)
				_sessionKey += (char) (rnd.nextInt(126 - 33) + 33);
			_sessionHash = HashUtils.SHA1(_sessionKey);
			_socket = socket;
		} catch (NoSuchAlgorithmException ex) {
			_sessionKey = "";
		} catch (UnsupportedEncodingException ex) {
			_sessionKey = "";
		} catch (Exception ex) {
			_sessionKey = "";
			throw new ServerSessionException(ex.getMessage());
		}
	}

	public boolean check(String hash) {
		_sessionStatus = _sessionHash.equalsIgnoreCase(hash);
		return _sessionStatus;
	}

	@Override
	public void close() throws IOException {
		_socket.close();
	}

	public InputStream getInputStream() throws IOException {
		return _socket.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return _socket.getOutputStream();
	}

	public String getSessionKey() {
		return _sessionKey;
	}

	public boolean getSessionStatus() {
		return _sessionStatus;
	}
}
