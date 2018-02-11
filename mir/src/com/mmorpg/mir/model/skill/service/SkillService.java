package com.mmorpg.mir.model.skill.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface SkillService {

	/**
	 * 使用技能
	 * 
	 * @param player
	 * @param skillId
	 * @param targetId
	 * @param x
	 * @param y
	 * @param targetList
	 * @param direction
	 */
	void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction);

	/**
	 * 攻击
	 * 
	 * @param player
	 * @param targetId
	 */
	void attack(Player player, long targetId);

	/**
	 * 技能升级
	 * 
	 * @param player
	 * @param skillId
	 */
	void skillLevelUp(Player player, int skillId);

	/**
	 * 学习被动技能
	 * 
	 * @param player
	 * @param skillId
	 */
	void learnPassiveSkill(Player player, int skillId);
}
