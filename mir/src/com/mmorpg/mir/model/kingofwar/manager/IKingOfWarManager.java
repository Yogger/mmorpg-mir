package com.mmorpg.mir.model.kingofwar.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Sculpture;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_Status;
import com.mmorpg.mir.model.kingofwar.packet.vo.CommandPlayerVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerRankInfoVO;



public interface IKingOfWarManager {
	void initAll();

	public void initSculptures();

	public void refreshSculptures(int countryValue);

	public List<Sculpture> getCountrySculpture(int countryValue);

	public void start();

	public void sendPackOnWar(Object packet);

	public ArrayList<PlayerRankInfoVO> getTopPlayerWarInfo(int start, int end);

	public ArrayList<CommandPlayerVO> getCommandPlayerVOs(Player player, int countryId);

	public void end(Player killBigBoss);

	public void reset();

	public void clearWarObject();

	public void sendPlayerCount();

	public ActionObserver kingOfWarSpawnObsever(final Player player);

	public void addReliveBuff(Player player);

	void removeReliveBuff(Player player);

	boolean isProtect();

	public void enterWar(Player player);

	public List<Player> getPlayerOnWar(int country);

	public void leaveWar(Player player);

	public long getNextKingOfWarTime();

	public long getOpenServerNextKingOfWarTime();

	public SM_KingOfWar_Status getKingOfWarStatus();

	public void setKingCommand(Player player, String command);

	public void rewardKingDaily();

	public boolean isKingOfKing(long playerId);

	public Player getKingOfKing();

	public long getBecomeKingOfKingTime();

	public Map<String, StatusNpc> getStatusNpcs();

	public List<VisibleObject> getVisibleObjects();

	public Map<Long, PlayerWarInfo> getPlayerWarInfos();

	public boolean isWarring();

	public void setWarring(boolean warring);

	public List<PlayerWarInfo> getRankTemp();

	public void forbidChat(Player player, Player target, int sign);

	public long getStartTime();

	public Map<CountryId, Long> getCommandCD();

	public void setCommandCD(Map<CountryId, Long> commandCD);

	public void refreshKingsWarship();

	public int getKingSupportCount();

	public int getKingContemptCount();

	public void supportKing();

	public void contemptKing();
}
