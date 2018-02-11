package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

/**
 * 追回营救次数
 * 
 * @author 37wan
 * 
 */
public class RescueClawbackEvent implements IEvent {

	private long playerId;// 用户ID
	private int num;// 追回的次数

	public static RescueClawbackEvent valueOf(long playerId,int num){
		RescueClawbackEvent event = new RescueClawbackEvent();
		event.setPlayerId(playerId);
		event.setNum(num);
		return event;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
