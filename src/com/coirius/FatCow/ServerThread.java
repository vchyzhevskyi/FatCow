/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private String _sessionKey = "";

	public ServerThread(Socket socket) {
		super("FatCowServerThread");
		try {
			_sessionKey = ServerSessionManager.getInstance().start(socket);
		} catch (ServerSessionException ex) {
			_sessionKey = "";
		}
	}

	@Override
	public void run() {
		try {
			if(_sessionKey.isEmpty())
				return;
			BufferedReader in = new BufferedReader(new InputStreamReader(ServerSessionManager.getInstance().getSession(_sessionKey).getInputStream()));
			PrintWriter out = new PrintWriter(ServerSessionManager.getInstance().getSession(_sessionKey).getOutputStream(), true);

			String str = "";

			while((str = in.readLine()) != null) {
				String[] parsedInputLine = str.split("(?<!\\\\)\\ ");
				if(parsedInputLine[0].equals("quit"))
					break;
				else if(parsedInputLine[0].equals("auth")) {
					if(parsedInputLine[1].equals("req"))
						out.println(ServerSessionManager.getInstance().getSession(_sessionKey).getSessionKey());
					else if(parsedInputLine[1].equals("chk"))
						if(ServerSessionManager.getInstance().getSession(_sessionKey).check(parsedInputLine[2]))
							out.println(ServerStatusCode.Success);
						else
							out.println(ServerStatusCode.Failed);
					else if(parsedInputLine[1].equals("sts"))
						if(ServerSessionManager.getInstance().getSession(_sessionKey).getSessionStatus())
							out.println(ServerStatusCode.Success);
						else
							out.println(ServerStatusCode.Failed);
					continue;
				}
				try {
					ServerModule module = (ServerModule) (Class.forName(ServerModuleManager.getInstance().get(parsedInputLine[0])).newInstance());
					if(module.getReqAuth() && !ServerSessionManager.getInstance().getSession(_sessionKey).getSessionStatus()) {
						out.println(ServerStatusCode.AuthenticationRequired);
						continue;
					}
					Object res = module.doWork(parsedInputLine);
					switch (res.getClass().getName()) {
						case "java.lang.String": {
							out.println((String) res);
							break;
						}
						case "[Ljava.lang.String;": {
							for (String s : (String[]) res)
								out.println(s);
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
				} catch (NullPointerException ex) {
					out.println(ServerStatusCode.Failed);
					ex.printStackTrace();
				}
			}

			ServerSessionManager.getInstance().stop(_sessionKey);
			in.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
