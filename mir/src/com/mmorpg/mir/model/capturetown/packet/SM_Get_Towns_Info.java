package com.mmorpg.mir.model.capturetown.packet;

import java.util.Map;

import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.capturetown.manager.TownManager;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Get_Towns_Info {
	private Map<String, Integer> townInfos;

	private String selfKey;
	private int count;
	private long townAcc;
	private long todayTotalReward;
	private long nextChanllengeTime;
	private int type; //是否可以继续挑战 0可以 1不行
	
	private int buyCount;
	private int clearCDCount;
	
	public static SM_Get_Towns_Info valueOf(Player player) {
		SM_Get_Towns_Info sm = new SM_Get_Towns_Info();
		sm.townInfos = TownManager.getInstance().getTownCountryInfo();
		sm.selfKey = player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
		sm.townAcc = TownManager.getInstance().getTownAccFeats(sm.selfKey);
		sm.todayTotalReward = player.getPlayerCountryHistory().getCaptureTownInfo().getTodayTotalReward();
		sm.nextChanllengeTime = player.getPlayerCountryHistory().getCaptureTownInfo().getNextChallengeTime();
		sm.type = player.getPlayerCountryHistory().getCaptureTownInfo().canEnter() ? 0 : 1;
		sm.count = TownConfig.getInstance().PLAYER_ENTER_DAILY_LIMIT.getValue() - 
				player.getPlayerCountryHistory().getCaptureTownInfo().getDailyCount();
		sm.buyCount = player.getPlayerCountryHistory().getCaptureTownInfo().getDailyBuyCount();
		sm.clearCDCount = player.getPlayerCountryHistory().getCaptureTownInfo().getClearCDCount();
		return sm;
	}

	public Map<String, Integer> getTownInfos() {
		return townInfos;
	}

	public void setTownInfos(Map<String, Integer> townInfos) {
		this.townInfos = townInfos;
	}

	public String getSelfKey() {
		return selfKey;
	}

	public void setSelfKey(String selfKey) {
		this.selfKey = selfKey;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTownAcc() {
		return townAcc;
	}

	public void setTownAcc(long townAcc) {
		this.townAcc = townAcc;
	}

	public long getTodayTotalReward() {
		return todayTotalReward;
	}

	public void setTodayTotalReward(long todayTotalReward) {
		this.todayTotalReward = todayTotalReward;
	}

	public long getNextChanllengeTime() {
		return nextChanllengeTime;
	}

	public void setNextChanllengeTime(long nextChanllengeTime) {
		this.nextChanllengeTime = nextChanllengeTime;
	}

	public final int getBuyCount() {
		return buyCount;
	}

	public final void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public final int getClearCDCount() {
		return clearCDCount;
	}

	public final void setClearCDCount(int clearCDCount) {
		this.clearCDCount = clearCDCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
