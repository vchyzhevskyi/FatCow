/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow;

public class ServerSessionException extends Exception {
	private static final long serialVersionUID = -508323106626815443L;

	public ServerSessionException() {
		super();
	}

	public ServerSessionException(String msg) {
		super(msg);
	}
}
