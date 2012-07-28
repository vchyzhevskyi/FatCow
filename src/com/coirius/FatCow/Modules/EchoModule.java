/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow.Modules;

import com.coirius.FatCow.ServerModule;

public class EchoModule extends ServerModule {
	@Override
	public Object doWork(String[] argv) {
		return "ECHO";
	}
}
