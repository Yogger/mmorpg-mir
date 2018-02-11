package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Draw_CelebrateReward {
	
	private int code;
	
	private Reward reward;

	public static SM_Draw_CelebrateReward valueOf(Reward r) {
		SM_Draw_CelebrateReward sm = new SM_Draw_CelebrateReward();
		sm.reward = r;
		return sm;
	}
	
	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
