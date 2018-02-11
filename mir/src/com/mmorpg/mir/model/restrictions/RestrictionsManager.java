package com.mmorpg.mir.model.restrictions;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.model.Skill;

/**
 * 权限管理器
 * 
 * @author 37 wan
 * 
 */
@Component
public final class RestrictionsManager implements IRestrictionsManager {

	/**
	 * Check whether player can use such skill
	 * 
	 * @param player
	 * @param skill
	 * @return
	 */
	public static boolean canUseSkill(Player player, Skill skill) {
		return true;
	}

	public static boolean canAttack(Player player, Creature target) {
		return true;
	}

	public static boolean canTrade(Player player1) {
		return true;
	}
	
	public boolean canInviteToGroup(Player player, Player target)
	{
		throw new AbstractMethodError();
	}

}
