package com.mmorpg.mir.model.country.packet;

import java.util.Map;

/**
 * 打开砍大臣界面
 * 
 * @author 37wan
 * 
 */
public class SM_Country_Open_Diplomacy {

	//private int surplusNum;// 剩余领奖次数
	//private long coolTime;// 冷却时间
	private Map<Integer, Boolean> countryNpcHpMap;// 每个国家大臣NPC的血量剩余百分比
	private Map<Integer, Integer> hiterCountry;
	private Map<Integer, Long> deadTime;

	public Map<Integer, Boolean> getCountryNpcHpMap() {
    	return countryNpcHpMap;
    }

	public void setCountryNpcHpMap(Map<Integer, Boolean> countryNpcHpMap) {
    	this.countryNpcHpMap = countryNpcHpMap;
    }

	public Map<Integer, Integer> getHiterCountry() {
    	return hiterCountry;
    }

	public void setHiterCountry(Map<Integer, Integer> hiterCountry) {
    	this.hiterCountry = hiterCountry;
    }

	public Map<Integer, Long> getDeadTime() {
    	return deadTime;
    }

	public void setDeadTime(Map<Integer, Long> deadTime) {
    	this.deadTime = deadTime;
    }

}
