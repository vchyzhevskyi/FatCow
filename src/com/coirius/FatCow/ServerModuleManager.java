/*
 * Copyright (C) 2012, Vladyslav Chyzhevskyi
 * Published under GNU GPL v2
 */

package com.coirius.FatCow;

import java.util.HashMap;

public class ServerModuleManager {
	private static final ServerModuleManager _moduleManager = new ServerModuleManager();
	private HashMap<String, ServerModule> _modules;

	public static ServerModuleManager getInstance() {
		return _moduleManager;
	}

	private ServerModuleManager() {
		_modules = new HashMap<String, ServerModule>();
	}

	public void register(ServerModule module) throws ServerModuleException {
		if(_modules.containsValue(module))
			throw new ServerModuleException(module.toString() + " already registered.");
		_modules.put(module.toString(), module);
	}

	public void register(String moduleKey, ServerModule module) throws ServerModuleException {
		if(_modules.containsKey(moduleKey))
			throw new ServerModuleException(moduleKey + " already registered.");
		_modules.put(moduleKey, module);
	}

	public void unregister(String moduleKey) throws ServerModuleException {
		if(!_modules.containsKey(moduleKey))
			throw new ServerModuleException(moduleKey + " not registered.");
		_modules.remove(moduleKey);
	}

	public ServerModule get(String moduleKey) throws ServerModuleException {
		if(!_modules.containsKey(moduleKey))
			throw new ServerModuleException(moduleKey + " not registered.");
		return _modules.get(moduleKey);
	}
}
