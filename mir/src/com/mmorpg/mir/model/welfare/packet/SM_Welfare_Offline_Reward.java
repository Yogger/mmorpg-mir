package com.mmorpg.mir.model.welfare.packet;

/**
 * 领取离线奖励
 * 
 * @author 37wan
 * 
 */
public class SM_Welfare_Offline_Reward {

	private int[] result;//[前端按钮类型,是否成功]

	public static SM_Welfare_Offline_Reward valueOf(int[] result) {
		SM_Welfare_Offline_Reward sm = new SM_Welfare_Offline_Reward();
		sm.setResult(result);
		return sm;
	}

	public int[] getResult() {
		return result;
	}

	public void setResult(int[] result) {
		this.result = result;
	}

}
