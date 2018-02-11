package com.mmorpg.mir.model.system.packet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MessageCache {

	public static final ConcurrentMap<Integer, SM_System_Message> cache = new ConcurrentHashMap<Integer, SM_System_Message>();

	public static final SM_System_Message getMessage(int code) {
		SM_System_Message msg = cache.get(code);
		if (msg == null) {
			SM_System_Message temp = new SM_System_Message();
			temp.setCode(code);
			msg = cache.putIfAbsent(code, temp);
			if (msg == null) {
				msg = temp;
			}
		}
		return msg;
	}

}
