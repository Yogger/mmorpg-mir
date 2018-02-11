package com.mmorpg.mir.model.i18n.manager;

import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.i18n.model.I18nPack;

public class I18nUtils {
	private String id;
	private Map<String, I18nPack> parms;

	public static I18nUtils valueOf(String id) {
		I18nUtils i18 = new I18nUtils();
		i18.id = id;
		i18.parms = New.hashMap();
		return i18;
	}
	public static I18nUtils valueOf(String i18nId, I18nUtils i18nUtils) {
		I18nUtils i18 = new I18nUtils();
		i18.id = i18nId;
		i18.parms = New.hashMap();
		for (Entry<String, I18nPack> entry: i18nUtils.parms.entrySet()) {
			i18.parms.put(entry.getKey(), entry.getValue().copyOf());
		}
		return i18;
	}

	public I18nUtils addParm(String key, I18nPack value) {
		parms.put(key, value);
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, I18nPack> getParms() {
		return parms;
	}

	public void setParms(Map<String, I18nPack> parms) {
		this.parms = parms;
	}

}
