package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_UseItem {
	private int code;
	private long nextTime;
	private Map<Integer, Object> packUpdate;
	private Reward reward;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

}
