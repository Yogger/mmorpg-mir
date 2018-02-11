package com.mmorpg.mir.model.welfare.packet;

/**
 * 推送给前端可领奖的数量
 * 
 * @author 37wan
 * 
 */

public class SM_Welfare_Push_Light_Reward {
	private int lightNum;// 福利大厅显示开放的数量

	public static SM_Welfare_Push_Light_Reward valueOf(int lightNum) {
		SM_Welfare_Push_Light_Reward sm = new SM_Welfare_Push_Light_Reward();
		sm.setLightNum(lightNum);
		return sm;
	}

	public int getLightNum() {
		return lightNum;
	}

	public void setLightNum(int lightNum) {
		this.lightNum = lightNum;
	}

}
