package com.coirius.FatCow;

import java.net.ServerSocket;
import java.io.IOException;

public class Server {
	public static void main(String[] args) {
		ServerSocket srvSock = null;
		boolean t = true;
		try {
			srvSock = new ServerSocket(65535);
			while(t)
				new ServerThread(srvSock.accept()).start();
			srvSock.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
