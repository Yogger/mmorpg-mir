package com.mmorpg.mir.model.country.model.vo;

import java.util.ArrayList;

public class CountryFlagQuestVO {
	// 是否在活动内
	private boolean isInAct;
	// 是否参加过国旗任务
	private boolean attendFlag;
	// 类型
	private int type;
	// 目标国家
	private int target;
	// 同盟国
	private int alliance;
	// 攻击方
	private ArrayList<Integer> attackCountries;
	// 开始时间
	private long startTime;
	// 下一次开始的时间
	private long nextStartTime;
	
	public boolean isInAct() {
		return isInAct;
	}

	public void setInAct(boolean isInAct) {
		this.isInAct = isInAct;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getAlliance() {
		return alliance;
	}

	public void setAlliance(int alliance) {
		this.alliance = alliance;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getNextStartTime() {
		return nextStartTime;
	}

	public void setNextStartTime(long nextStartTime) {
		this.nextStartTime = nextStartTime;
	}

	public ArrayList<Integer> getAttackCountries() {
		return attackCountries;
	}

	public void setAttackCountries(ArrayList<Integer> attackCountries) {
		this.attackCountries = attackCountries;
	}

	public boolean isAttendFlag() {
		return attendFlag;
	}

	public void setAttendFlag(boolean attendFlag) {
		this.attendFlag = attendFlag;
	}

}
