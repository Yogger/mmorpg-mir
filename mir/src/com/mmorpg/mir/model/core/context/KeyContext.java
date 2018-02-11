package com.mmorpg.mir.model.core.context;

import java.util.Map;

public class KeyContext {
	private String key;
	private Map<String, Object> context;
	private int mount = 1;

	private KeyContext(String key, Map<String, Object> context, int mount) {
		this.key = key;
		this.context = context;
		this.mount = mount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public int getMount() {
		return mount;
	}

	public void setMount(int mount) {
		this.mount = mount;
	}

	public static KeyContext valueOf(String key) {
		return new KeyContext(key, null, 1);
	}

	public static KeyContext valueOf(String key, Map<String, Object> context) {
		return new KeyContext(key, context, 1);
	}

	public static KeyContext valueOf(String key, int mount) {
		return new KeyContext(key, null, mount);
	}

	public static KeyContext valueOf(String key, Map<String, Object> context, int mount) {
		return new KeyContext(key, context, mount);
	}
}
