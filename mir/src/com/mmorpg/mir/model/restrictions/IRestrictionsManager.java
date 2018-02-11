package com.mmorpg.mir.model.restrictions;

import com.mmorpg.mir.model.gameobjects.Player;

public interface IRestrictionsManager {
	boolean canInviteToGroup(Player player, Player target);
}
