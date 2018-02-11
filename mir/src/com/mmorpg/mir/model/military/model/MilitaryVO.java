package com.mmorpg.mir.model.military.model;

import java.util.HashMap;
import java.util.Map;


public class MilitaryVO {

	private int rank;
	
	private String starId;

	private Map<Integer, Integer> strategyMap;
	
	private Map<Integer, Long> upgradeTimeLog;
	
	private Map<Integer, Map<Integer, String>> upgradeEquipLog;
	
	public static MilitaryVO valueOf(Military m) {
		MilitaryVO vo = new MilitaryVO();
		vo.rank = m.getRank();
		vo.strategyMap = new HashMap<Integer, Integer>(m.getStrategy());
		vo.upgradeTimeLog = m.getUpgradeTimeLog();
		vo.upgradeEquipLog = m.getUpgradeEquipLog(); 
		vo.starId = m.getStarId();
		return vo;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Map<Integer, Integer> getStrategyMap() {
		return strategyMap;
	}

	public void setStrategyMap(Map<Integer, Integer> strategyMap) {
		this.strategyMap = strategyMap;
	}

	public Map<Integer, Long> getUpgradeTimeLog() {
		return upgradeTimeLog;
	}

	public void setUpgradeTimeLog(Map<Integer, Long> upgradeTimeLog) {
		this.upgradeTimeLog = upgradeTimeLog;
	}

	public Map<Integer, Map<Integer, String>> getUpgradeEquipLog() {
		return upgradeEquipLog;
	}

	public void setUpgradeEquipLog(Map<Integer, Map<Integer, String>> upgradeEquipLog) {
		this.upgradeEquipLog = upgradeEquipLog;
	}

	public String getStarId() {
    	return starId;
    }

	public void setStarId(String starId) {
    	this.starId = starId;
    }

}
