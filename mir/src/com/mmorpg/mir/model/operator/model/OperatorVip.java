package com.mmorpg.mir.model.operator.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class OperatorVip {

	private transient Player owner;
	/** 已经领取 */
	private Map<Integer, Boolean> rewarded;
	/** 平台VIP等级 */
	private int level;

	/**
	 *  360游戏大厅玩家每登录一天，即可领取1次礼包（每天00:00重置）  每天最多可领取1次礼包，可领取状态不可跨天累积 
	 * 玩家最多可领取7次礼包，7次礼包领取后，图标消失
	 */
	/** 最后一次领取360大厅礼包的时间 */
	private long lastReward360giftTime;
	/** 总共领取360大厅礼包的时间 */
	private int count360gift;
	// once
	private Map<Integer, Boolean> nickNameRewarded;
	// daily
	private Map<Integer, Boolean> levelRewarded;
	// last daily reward time
	private long dailyRewardTime;

	private long lastClearTime;

	public static OperatorVip valueOf() {
		OperatorVip telphone = new OperatorVip();
		telphone.rewarded = new HashMap<Integer, Boolean>();
		telphone.nickNameRewarded = new HashMap<Integer, Boolean>();
		telphone.levelRewarded = new HashMap<Integer, Boolean>();
		return telphone;
	}

	public void dailyClear() {
		lastClearTime = new Date().getTime();
		levelRewarded.clear();
	}

	@JsonIgnore
	public void addCount360gift() {
		count360gift++;
		lastReward360giftTime = System.currentTimeMillis();
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Map<Integer, Boolean> getRewarded() {
		return rewarded;
	}

	public void setRewarded(Map<Integer, Boolean> rewarded) {
		this.rewarded = rewarded;
	}

	public long getLastReward360giftTime() {
		return lastReward360giftTime;
	}

	public void setLastReward360giftTime(long lastReward360giftTime) {
		this.lastReward360giftTime = lastReward360giftTime;
	}

	public int getCount360gift() {
		return count360gift;
	}

	public void setCount360gift(int count360gift) {
		this.count360gift = count360gift;
	}

	public long getLastClearTime() {
		return lastClearTime;
	}

	public void setLastClearTime(long lastClearTime) {
		this.lastClearTime = lastClearTime;
	}

	public Map<Integer, Boolean> getNickNameRewarded() {
		return nickNameRewarded;
	}

	public void setNickNameRewarded(Map<Integer, Boolean> nickNameRewarded) {
		this.nickNameRewarded = nickNameRewarded;
	}

	public Map<Integer, Boolean> getLevelRewarded() {
		return levelRewarded;
	}

	public void setLevelRewarded(Map<Integer, Boolean> levelRewarded) {
		this.levelRewarded = levelRewarded;
	}

	public long getDailyRewardTime() {
		return dailyRewardTime;
	}

	public void setDailyRewardTime(long dailyRewardTime) {
		this.dailyRewardTime = dailyRewardTime;
	}
}
