package com.mmorpg.mir.model.combatspirit.packet;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;

public class SM_CombatSpirit_Reward {

	private byte type;
	private int addValue;
	private int currentValue;

	private int historyValue;
	private int todayValue;
	
	public static SM_CombatSpirit_Reward valueOf(CombatSpirit cs, int amount) {
		SM_CombatSpirit_Reward sm = new SM_CombatSpirit_Reward();
		sm.addValue = amount;
		sm.type = (byte) cs.getType();
		sm.currentValue = cs.getGrowUpValue();
		sm.historyValue = cs.getHistoryValue();
		sm.todayValue = cs.getTodayValue();
		return sm;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getAddValue() {
		return addValue;
	}

	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getHistoryValue() {
		return historyValue;
	}

	public void setHistoryValue(int historyValue) {
		this.historyValue = historyValue;
	}

	public int getTodayValue() {
		return todayValue;
	}

	public void setTodayValue(int todayValue) {
		this.todayValue = todayValue;
	}

}