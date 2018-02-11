package com.mmorpg.mir.model.chat.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mmorpg.mir.model.i18n.model.I18nPack;

/**
 * 2.2.1 综合频道 2 2.2.2 世界频道 2 2.2.3 国家频道（独立子聊天频道） 2 2.2.4 家族频道 3 2.2.5 队伍频道 3
 * 2.2.6 私聊频道 3 2.2.7 交易频道 3 2.2.8 喇叭频道 3
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-12
 * 
 */
public class Message {
	private int channel;
	private String content;
	private String i18n;
	private ConcurrentHashMap<String, I18nPack> i18nParms;
	private Sender sender;
	private long reciver;

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}

	public Map<String, I18nPack> getI18nParms() {
		return i18nParms;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public void setI18nParms(ConcurrentHashMap<String, I18nPack> i18nParms) {
		this.i18nParms = i18nParms;
	}

	public long getReciver() {
		return reciver;
	}

	public void setReciver(long reciver) {
		this.reciver = reciver;
	}

}
