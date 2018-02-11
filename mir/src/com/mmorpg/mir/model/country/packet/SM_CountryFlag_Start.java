package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;

public class SM_CountryFlag_Start {
	
	private ArrayList<Integer> attackCountries;
	/** 任务 目标国家 */
	private int target;
	/** 任务 的同盟国 如果是2V1的类型的话 */
	private int alliance;
	/** 开始的时间 */
	private long startTime;
	
	public static SM_CountryFlag_Start valueOf(ArrayList<Integer> countries, int t, int a, long start) {
		SM_CountryFlag_Start vo = new SM_CountryFlag_Start();
		vo.attackCountries = countries;
		vo.target = t;
		vo.alliance = a;
		vo.startTime = start;
		return vo;
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

	public ArrayList<Integer> getAttackCountries() {
		return attackCountries;
	}

	public void setAttackCountries(ArrayList<Integer> attackCountries) {
		this.attackCountries = attackCountries;
	}

}
