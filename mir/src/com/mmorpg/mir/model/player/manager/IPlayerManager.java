package com.mmorpg.mir.model.player.manager;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.model.PlayerExpUpdate;
import com.mmorpg.mir.model.player.resource.PlayerLevelResource;

public interface IPlayerManager {
	public void init();

	public void logout(LogoutEvent event);

	public String getStandardExp(Player player);

	public String getStandardCoins(Player player);

	public String getStandardIncr(Player player);

	public boolean isIndexExist(String account, String op, String server);

	public boolean isNameExist(String name);

	public PlayerLevelResource getPlayerLevelResource(int id);

	public Player createPlayer(String account, String playerName, int role, String server, String op, int country,
			final String source, final int noframe);

	public int selectCountry();

	public int selectRole();

	public void resetPlayerGameStats(Player player);

	public PlayerExpUpdate levelUp(Player player);

	public PlayerExpUpdate addExp(Player player, long exp, boolean log, ModuleInfo moduleInfo);

	public Player getPlayerByAccount(String account, String op, String server);

	public Player getPlayer(long playerId);

	public void updatePlayer(Player player);

	public boolean serialize(PlayerEnt ent);

	public PlayerEnt getByName(String playerName);

	public void registerHandle(ModuleHandle playerInitUpdateHandle);

	public boolean validate(String account, String op, String server, String sign, String time, int opVip);

	public ConfigValue<Integer> getPK_DURATION_TIME();

	public void setPK_DURATION_TIME(ConfigValue<Integer> pK_DURATION_TIME);

	public CoreConditions getDeadNoticeConditions();
}
