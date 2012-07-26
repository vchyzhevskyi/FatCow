package com.coirius.FatCow;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private int _sessionId = -1;

	public ServerThread(Socket socket) {
		super("FatCowServerThread");
		try {
			_sessionId = ServerSessionManager.getInstance().start(socket);
		} catch (ServerSessionException ex) {
			_sessionId = -1;
		}
	}

	@Override
	public void run() {
		try {
			if(_sessionId == -1)
				return;
			BufferedReader in = new BufferedReader(new InputStreamReader(ServerSessionManager.getInstance().getSession(_sessionId).getInputStream()));
			PrintWriter out = new PrintWriter(ServerSessionManager.getInstance().getSession(_sessionId).getOutputStream(), true);
			
			out.println(ServerSessionManager.getInstance().getCount());

			String str = "";
			out.println(ServerSessionManager.getInstance().getSession(_sessionId).getAuthKey());

			while((str = in.readLine()) != null) {
				out.println(str);
			}

			ServerSessionManager.getInstance().stop(_sessionId);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
