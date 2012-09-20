/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow;

public abstract class ServerModule {
	public Object doWork(String[] argv) {
		return null;
	}

	public boolean getReqAuth() {
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
