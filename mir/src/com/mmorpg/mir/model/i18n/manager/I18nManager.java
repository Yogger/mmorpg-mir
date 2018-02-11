package com.mmorpg.mir.model.i18n.manager;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.i18n.resource.I18NResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class I18nManager implements II18nManager{
	@Static
	private Storage<String, I18NResource> I18NResources;

	private static I18nManager instance;

	@PostConstruct
	public void init() {
		setInstance(this);
	}

	public String getI18n(String key) {
		return I18NResources.get(key, true).getValue();
	}

	/**
	 * 带参数获取
	 * 
	 * @param key
	 * @param parms
	 * @return
	 */
	// public String getI18n(String key, Map<String, String> parms) {
	// String or = I18NResources.get(key, true).getValue();
	// String newStr = new String(or);
	// for (Entry<String, String> entry : parms.entrySet()) {
	// newStr = newStr.replace("{" + entry.getKey() + "}", entry.getValue());
	// }
	// return newStr;
	// }
	//
	// public Object getI18n(I18nUtils utils) {
	// return this.getI18n(utils.getId(), utils.getParms());
	// }

	public static I18nManager getInstance() {
		return instance;
	}

	public static void setInstance(I18nManager instance) {
		I18nManager.instance = instance;
	}

}
