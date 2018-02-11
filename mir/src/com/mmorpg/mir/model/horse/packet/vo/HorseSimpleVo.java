package com.mmorpg.mir.model.horse.packet.vo;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.model.HorseAppearance;

public class HorseSimpleVo {
	/** 阶数 */
	private int grade;
	/** 等级 */
	private int level;
	/** 坐骑外观 */
	private HorseAppearance horseAppearance;
	/** 坐骑道具数量 */
	private Map<String, Integer> enhanceItemCount;
	/** 坐骑学的技能 */
	private Map<Integer, Integer> learnedSkills;
	/**成长丹*/
	private Map<String, Integer> growItemCount;

	public static HorseSimpleVo valueOf(Player player) {
		HorseSimpleVo result = new HorseSimpleVo();
		result.grade = player.getHorse().getGrade();
		result.level = player.getHorse().getLevel();
		result.horseAppearance = player.getHorse().getAppearance();
		result.enhanceItemCount = player.getHorse().getEnhanceItemCount();
		result.learnedSkills = player.getHorse().getLearnedSkills();
		result.growItemCount = player.getHorse().getGrowItemCount();
		return result;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public HorseAppearance getHorseAppearance() {
		return horseAppearance;
	}

	public void setHorseAppearance(HorseAppearance horseAppearance) {
		this.horseAppearance = horseAppearance;
	}

	public Map<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(Map<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

	public Map<Integer, Integer> getLearnedSkills() {
		return learnedSkills;
	}

	public void setLearnedSkills(Map<Integer, Integer> learnedSkills) {
		this.learnedSkills = learnedSkills;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}

}
