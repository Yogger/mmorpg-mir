package com.mmorpg.mir.model.restrictions;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;

public interface IRestrictions {
	public boolean isRestricted(Player player, Class<? extends IRestrictions> callingRestriction);

	public boolean canAttack(Player player, VisibleObject target);

	public boolean canAffectBySkill(Player player, VisibleObject target);

	public boolean canUseSkill(Player player, Skill skill);

	public boolean canChat(Player player);

	public boolean canInviteToGroup(Player player, Player target);

	public boolean canInviteToAlliance(Player player, Player target);

	public boolean canChangeEquip(Player player);

	public boolean canUseWarehouse(Player player);

	public boolean canTrade(Player player);

	public boolean canUseItem(Player player);
}
