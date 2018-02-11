package com.mmorpg.mir.model.mergeactive.model;

import java.util.HashMap;
import java.util.Map;

public class LoginGift {
	Map<String, Long> drawLog = new HashMap<String, Long>();
	
	public static LoginGift valueOf(){
		LoginGift gift = new LoginGift();
		gift.drawLog = new HashMap<String, Long>();
		return gift;
	}
	
	public void addDrawLog(String id){
		drawLog.put(id, System.currentTimeMillis());
	}

	public boolean hasDrawBefore(String id){
		return drawLog.containsKey(id);
	}
	
	
	public Map<String, Long> getDrawLog() {
		return drawLog;
	}
}
