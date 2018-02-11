package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;

/**
 * 至尊追回
 * 
 * @author 37wan
 * 
 */
public class SM_Welfare_Clawback_Reward_Superme {

	ArrayList<Integer> succList;// 成功追回的收益事件Id

	public static SM_Welfare_Clawback_Reward_Superme valueOf(ArrayList<Integer> succList) {
		SM_Welfare_Clawback_Reward_Superme sm = new SM_Welfare_Clawback_Reward_Superme();
		sm.setSuccList(succList);
		return sm;
	}

	public ArrayList<Integer> getSuccList() {
		return succList;
	}

	public void setSuccList(ArrayList<Integer> succList) {
		this.succList = succList;
	}
}
