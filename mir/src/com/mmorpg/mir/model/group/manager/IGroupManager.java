package com.mmorpg.mir.model.group.manager;

import com.mmorpg.mir.model.gameobjects.Player;

public interface IGroupManager {
	int getGroupMaxLimit();

	void getPlayerGroupList(Player player);

	void apply(Player player, Player leader);

	boolean isGroupMember(long playerObjId);

	void getAppyList(Player player);

	void onLogin(Player activePlayer);

	void cancelScheduleRemove(long playerObjId);

	void removePlayerFromGroup(Player player, boolean force);

	void exple(Player leader, Player target);

	void changeLeader(Player leader, Player target);

	void scheduleRemove(final Player player);

	void dealApply(Player leader, long invitedId, boolean ok);

	void createPlayerGroup(Player inviter);

	void invitePlayerToGroup(final Player inviter, final Player invited);
}
