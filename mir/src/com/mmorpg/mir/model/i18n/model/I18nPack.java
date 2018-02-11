package com.mmorpg.mir.model.i18n.model;

import java.util.ArrayList;

public class I18nPack {
	private byte type;
	// 为了通信层能够动态的读取Object信息所以封装的对象
	private ArrayList<Object> objects = new ArrayList<Object>();

	public static I18nPack valueOf(Object object) {
		I18nPack i18pack = new I18nPack();
		if (object instanceof I18nPackItem) {
			// 复合类型
			i18pack.type = ((I18nPackItem) object).getMessageType();
		} else {
			// 字符串类型
			i18pack.type = I18nPackType.STRING.getValue();
		}
		i18pack.objects.add(object);
		return i18pack;
	}

	public I18nPack copyOf() {
		I18nPack i18nPack = new I18nPack();
		i18nPack.type = type;
		i18nPack.objects = new ArrayList<Object>(objects);
		return i18nPack;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public ArrayList<Object> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<Object> objects) {
		this.objects = objects;
	}

}
