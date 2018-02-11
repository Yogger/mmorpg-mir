package com.mmorpg.mir.model.chat.packet;

import java.util.HashMap;

public class CM_Chat_Request {
	private int channelId;
	/** i18n id */
	private String i18n;
	/** 内容 */
	private String content;
	/** i18n 参数 */
	private HashMap<String, String> i18nParms;
	/** 展示物品 */
	private HashMap<String, HashMap<Integer, HashMap<String, String>>> objects;
	/** 接收者的id，私聊是才发送，其他类型的通信为0 */
	private long reciverId;

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}

	public HashMap<String, String> getI18nParms() {
		return i18nParms;
	}

	public void setI18nParms(HashMap<String, String> i18nParms) {
		this.i18nParms = i18nParms;
	}

	public HashMap<String, HashMap<Integer, HashMap<String, String>>> getObjects() {
		return objects;
	}

	public void setObjects(HashMap<String, HashMap<Integer, HashMap<String, String>>> objects) {
		this.objects = objects;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getReciverId() {
		return reciverId;
	}

	public void setReciverId(long reciverId) {
		this.reciverId = reciverId;
	}

}
