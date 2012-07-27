package com.coirius.FatCow;

public abstract class ServerModule {
	public Object doWork(Object argv) {
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
}