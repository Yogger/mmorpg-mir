package com.mmorpg.mir.model.gang.manager;

import java.util.concurrent.ConcurrentHashMap;

import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.packet.SM_Gang_List;

 interface IGangManager {
	void init();

	void setAutoDeal(Player player, boolean autoDeal);

	Gang apply(Player player, long id, boolean byPlayerId);

	Gang applyCancel(Player player, long id);

	void changePosition(Player master, long targetId, int position);

	Gang createGang(Player player, String gangName);

	void dealApply(Player player, long id, boolean ok);

	void disband(Player master);

	void expel(Player master, long targetId);

	Gang get(long gangId);

	void sendMessage(Object message, long gangId, long senderId);

	void getGangApplyList(Player player);

	void getGangDetailInfo(Player player);

	SM_Gang_List getGangSimpleList();

	SM_Gang_List getGangSimpleList(Country country);

	SM_Gang_List getGangSimpleList(Country country, String gangName);

	SM_Gang_List getOtherCountryGangSimpleList(Player player);

	void invite(Player master, Player target);

	void dealInvite(Player player, long gangId, boolean ok);

	void playerLogin(Player player);

	 void quit(Player player);

	 void refreshLastLoginOut(Player player);

	 void setGangInfo(Player master, String infor);

	 void impeachGangMaster(Player player);

	 void doLevelUpRefresh(long gangId, Player player);
	 
	 void doVipRefresh(long gangId, Player player);
	 
	 void doPromotionRefresh(long gangId, Player player);

	 ConcurrentHashMap<Long, Gang> getGangs();

	 void askForHelp(final Player player, int sign);
}
