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

			String str = "";

			while((str = in.readLine()) != null) {
				String[] parsedInputLine = str.split("(?<!\\\\)\\ ");
				if(parsedInputLine[0].equalsIgnoreCase("quit"))
					break;
				try {
					ServerModule module = (ServerModule) (Class.forName(ServerModuleManager.getInstance().get(parsedInputLine[0]).toString()).newInstance());
					Object res = module.doWork(parsedInputLine);
					switch (res.getClass().getName()) {
						case "java.lang.String": {
							out.println((String) res);
							break;
						}
						case "[Ljava.lang.String;": {
							for (String s : (String[]) res)
								out.println(str);
							break;
						}
						case "java.lang.Boolean": {
							if((boolean) res)
								out.println(ServerStatusCode.Success);
							else
								out.println(ServerStatusCode.Failed);
							break;
						}
						default: {
							out.println(ServerStatusCode.UnknownResultType);
							break;
						}
					}
				} catch (ClassNotFoundException ex) {
					out.println(ServerStatusCode.UnknownModule);
					ex.printStackTrace();
				} catch (InstantiationException ex) {
					ex.printStackTrace();
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				} catch (ServerModuleException ex) {
					out.println(ex.getMessage());
				}
			}

			ServerSessionManager.getInstance().stop(_sessionId);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
