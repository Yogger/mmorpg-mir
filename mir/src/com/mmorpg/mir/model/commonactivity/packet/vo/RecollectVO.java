package com.mmorpg.mir.model.commonactivity.packet.vo;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.model.ClawbackStatus;
import com.mmorpg.mir.model.commonactivity.model.RecollectType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;

public class RecollectVO {
	/**
	 * 0.未开启 1.已经追回; 2.可追回; 3. 不可追回
	 */
	private int status;// 状态
	
	private int eventId; // ID
	
	private int count;//可以找回的次数
	
	private CoreActions copperActions;
	
	private CoreActions goldActions;
	
	private Reward reward;
	
	public static RecollectVO valueOf(Player player, RecollectType type) {
		RecollectVO vo = new RecollectVO();
		vo.eventId = type.getValue();
		vo.status = player.getCommonActivityPool().getCurrentRecollectActive().getClawStatus(player, type).getValue();
		if (vo.status != ClawbackStatus.MODULE_NOT_OPEN.getValue()) {
			int count = player.getCommonActivityPool().getCurrentRecollectActive().getCanClawbackCount(player, type);
			vo.count = count;
			vo.copperActions = CommonActivityConfig.getInstance().getRecollectCopperActions(type, count);
			vo.goldActions = CommonActivityConfig.getInstance().getRecollectGoldActions(type, count);
			vo.reward = CommonActivityConfig.getInstance().getRecollectRewards(player, type, count);
		}		
		return vo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public CoreActions getCopperActions() {
		return copperActions;
	}

	public void setCopperActions(CoreActions copperActions) {
		this.copperActions = copperActions;
	}

	public CoreActions getGoldActions() {
		return goldActions;
	}

	public void setGoldActions(CoreActions goldActions) {
		this.goldActions = goldActions;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

}
