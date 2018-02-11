package com.mmorpg.mir.model.achievement.model;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Achievement {
	/** 所有成就 */
	private NonBlockingHashMap<Integer, AchievementItem> achievements;

	@JsonIgnore
	public AchievementItem getOrCreate(int id) {
		if (achievements.containsKey(id)) {
			return achievements.get(id);
		}
		return achievements.putIfAbsent(id, AchievementItem.valueOf(id));
	}

	/**
	 * 是否领奖
	 * 
	 * @param id
	 * @return
	 */
	@JsonIgnore
	public boolean isRewarded(int id) {
		if (!achievements.containsKey(id)) {
			return false;
		}
		return achievements.get(id).isRewarded();
	}

	/**
	 * 是否完成
	 * 
	 * @param id
	 * @return
	 */
	@JsonIgnore
	public boolean isCompleted(int id) {
		if (!achievements.containsKey(id)) {
			return false;
		}
		return achievements.get(id).isCompleted();
	}

	public NonBlockingHashMap<Integer, AchievementItem> getAchievements() {
		return achievements;
	}

	public void setAchievements(NonBlockingHashMap<Integer, AchievementItem> achievements) {
		this.achievements = achievements;
	}

	public static void main(String[] args) {
		NonBlockingHashMap<Integer, String> ss = new NonBlockingHashMap<Integer, String>();
		ss.put(1, "11");
		ss.put(2, "22");
		ss.put(3, "33");
		System.out.println(ss.putIfAbsent(1, "111"));
		System.out.println(ss.get(1));
	}

}
