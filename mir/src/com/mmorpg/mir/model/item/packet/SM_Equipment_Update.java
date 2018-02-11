package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.item.Equipment;

public class SM_Equipment_Update {
	private int code;
	private Equipment equip;
	private Map<String, Integer> luckyPoints;
	private Map<String, Long> luckyPointsTime;
	private int addLucky;
	
	public static SM_Equipment_Update failReturn(Map<String, Integer> luckyPoints, Map<String, Long> luckyPointsTime, int code, int lucky) {
		SM_Equipment_Update ret = new SM_Equipment_Update();
		ret.code = code;
		ret.luckyPoints = luckyPoints;
		ret.luckyPointsTime = luckyPointsTime;
		ret.addLucky = lucky;
		return ret;
	}
	
	public static SM_Equipment_Update valueOf(Map<String, Integer> luckyPoints, Map<String, Long> luckyPointsTime, Equipment equip) {
		SM_Equipment_Update ret = new SM_Equipment_Update();
		ret.luckyPoints = luckyPoints;
		ret.luckyPointsTime = luckyPointsTime;
		ret.equip = equip;
		return ret;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Equipment getEquip() {
		return equip;
	}

	public void setEquip(Equipment equip) {
		this.equip = equip;
	}

	public Map<String, Integer> getLuckyPoints() {
		return luckyPoints;
	}

	public void setLuckyPoints(Map<String, Integer> luckyPoints) {
		this.luckyPoints = luckyPoints;
	}

	public Map<String, Long> getLuckyPointsTime() {
		return luckyPointsTime;
	}

	public void setLuckyPointsTime(Map<String, Long> luckyPointsTime) {
		this.luckyPointsTime = luckyPointsTime;
	}

	public int getAddLucky() {
		return addLucky;
	}

	public void setAddLucky(int addLucky) {
		this.addLucky = addLucky;
	}
	
}
