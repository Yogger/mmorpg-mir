package com.mmorpg.mir.model.chat.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

public class ChatCoolTime {
	private Map<Integer, Long> cooltimes = New.hashMap();

	@JsonIgnore
	public boolean isCd(int channelId) {
		if (!cooltimes.containsKey(channelId)) {
			return false;
		}
		if (cooltimes.get(channelId) > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void putCd(int channelId, long cd) {
		long cdTime = cd + System.currentTimeMillis();
		cooltimes.put(channelId, cdTime);
	}

	public Map<Integer, Long> getCooltimes() {
		return cooltimes;
	}

	public void setCooltimes(Map<Integer, Long> cooltimes) {
		this.cooltimes = cooltimes;
	}

}
