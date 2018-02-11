package com.mmorpg.mir.model.achievement.model;

public class AchievementItem {
	private int id;
	/** 是否已经领取奖励 */
	private boolean rewarded;
	/** 已经完成 */
	private boolean completed;

	public static AchievementItem valueOf(int id) {
		AchievementItem item = new AchievementItem();
		item.id = id;
		return item;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isRewarded() {
		return rewarded;
	}

	public void setRewarded(boolean rewarded) {
		this.rewarded = rewarded;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
