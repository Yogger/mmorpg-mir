package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.welfare.packet.vo.OnlineRewardVO;

public class SM_Welfare_Online_Reward {

	private ArrayList<OnlineRewardVO> onlineRewardVOs;

	public static SM_Welfare_Online_Reward valueOf(ArrayList<OnlineRewardVO> onlineRewardVOs) {
		SM_Welfare_Online_Reward sm = new SM_Welfare_Online_Reward();
		sm.setOnlineRewardVOs(onlineRewardVOs);
		return sm;
	}

	public ArrayList<OnlineRewardVO> getOnlineRewardVOs() {
		return onlineRewardVOs;
	}

	public void setOnlineRewardVOs(ArrayList<OnlineRewardVO> onlineRewardVOs) {
		this.onlineRewardVOs = onlineRewardVOs;
	}

}
