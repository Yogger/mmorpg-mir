package com.mmorpg.mir.model.beauty.packet.vo;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.beauty.model.BeautyGirl;
import com.mmorpg.mir.model.beauty.model.BeautyGirlPool;

public class BeautyGirlPoolVo {
	private String fightGirl;
	private HashMap<String, BeautyGirl> beautyGrils;

	private Map<String, Integer> itemCounts;

	public static BeautyGirlPoolVo valueOf(BeautyGirlPool pool) {
		BeautyGirlPoolVo result = new BeautyGirlPoolVo();
		result.fightGirl = pool.getFightingGirlId();
		result.beautyGrils = new HashMap<String, BeautyGirl>(pool.getBeautyGirls());
		result.itemCounts = new HashMap<String, Integer>(pool.getItemCounts());
		return result;
	}

	public String getFightGirl() {
		return fightGirl;
	}

	public HashMap<String, BeautyGirl> getBeautyGrils() {
		return beautyGrils;
	}

	public void setFightGirl(String fightGirl) {
		this.fightGirl = fightGirl;
	}

	public void setBeautyGrils(HashMap<String, BeautyGirl> beautyGrils) {
		this.beautyGrils = beautyGrils;
	}

	public Map<String, Integer> getItemCounts() {
		return itemCounts;
	}

	public void setItemCounts(Map<String, Integer> itemCounts) {
		this.itemCounts = itemCounts;
	}

}
