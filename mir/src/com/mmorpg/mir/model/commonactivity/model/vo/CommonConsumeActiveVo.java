package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.ArrayList;
import java.util.HashSet;

import com.mmorpg.mir.model.commonactivity.manager.CommonActivityManager;
import com.mmorpg.mir.model.commonactivity.model.CommonConsumeActive;
import com.mmorpg.mir.model.gameobjects.Player;

public class CommonConsumeActiveVo {

	private String activityName;
	// 活动期间消费的元宝
	private int consumeGold;

	private HashSet<String> rewarded;

	private ArrayList<String> canReceives;

	public static CommonConsumeActiveVo valueOf(Player player, CommonConsumeActive value) {
		CommonConsumeActiveVo result = new CommonConsumeActiveVo();
		result.activityName = value.getActivityName();
		result.consumeGold = value.getConsumeGold();
		result.rewarded = value.getRewarded();
		result.canReceives = CommonActivityManager.getInstance().getConsumeCanRecievesReward(player,
				value.getActivityName());
		return result;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getConsumeGold() {
		return consumeGold;
	}

	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public ArrayList<String> getCanReceives() {
		return canReceives;
	}

	public void setCanReceives(ArrayList<String> canReceives) {
		this.canReceives = canReceives;
	}

}
