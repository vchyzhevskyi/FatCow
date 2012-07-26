package com.coirius.FatCow;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket _socket;

	public ServerThread(Socket socket) {
		_socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			PrintWriter out = new PrintWriter(_socket.getOutputStream());

			String str = "";

			while((str = in.readLine()) != null) {
				out.println(str);
			}

			_socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}