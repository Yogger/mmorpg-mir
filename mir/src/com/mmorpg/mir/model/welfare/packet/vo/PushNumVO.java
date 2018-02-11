package com.mmorpg.mir.model.welfare.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;

/**
 * 用于前端显示事件进度
 * 
 * @author 37wan
 * 
 */
public class PushNumVO {

	private int eventId; // 事件id
	private int clawbackNum;// 已追回次数
	private int currentCount; // 

	public static PushNumVO valueOf(ClawbackEnum claw, Player player) {
		PushNumVO vo = new PushNumVO();
		vo.setEventId(claw.getEventId());
		vo.setClawbackNum(player.getWelfare().getWelfareHistory().getClawbackNum(claw));
		if (claw.getEventId() == ClawbackEnum.CLAWBACK_EVENT_EXPRESS.getEventId()) {
			vo.currentCount = player.getExpress().getLorryCount();
		} else if (claw.getEventId() == ClawbackEnum.CLAWBACK_EVENT_TEMPLE.getEventId()) {
			vo.currentCount = player.getTempleHistory().getCount();
		} else if (claw.getEventId() == ClawbackEnum.CLAWBACK_EVENT_INVESTIGATE.getEventId()) {
			vo.currentCount = player.getInvestigate().getCount();
		}
		return vo;
	}

	@Override
	public String toString() {
		return eventId + " = " + eventId + " , clawbackNum = " + clawbackNum;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getClawbackNum() {
		return clawbackNum;
	}

	public void setClawbackNum(int clawbackNum) {
		this.clawbackNum = clawbackNum;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	
}
