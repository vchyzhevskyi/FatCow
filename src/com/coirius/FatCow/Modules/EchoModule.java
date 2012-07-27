package com.coirius.FatCow.Modules;

import com.coirius.FatCow.ServerModule;

public class EchoModule extends ServerModule {
	@Override
	public Object doWork(Object argv) {
		return "ECHO";
	}
}