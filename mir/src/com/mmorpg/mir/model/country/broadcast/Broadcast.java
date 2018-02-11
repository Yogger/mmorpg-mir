package com.mmorpg.mir.model.country.broadcast;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;

/**
 * 国家公告
 * 
 * @author 37wan
 * 
 */
public class Broadcast {

	private Broadcast() {
	}

	private static class Instance {
		private static Broadcast instance = new Broadcast();
	}

	public static Broadcast getInstance() {
		return Instance.instance;
	}

	// 电视

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 电视 : 广播我国战士正在浴血奋战大臣 */
	public void broadcastCallByFightingDip(Player hiter, Country targetCountry) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("10301");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(targetCountry.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(targetCountry.getDiplomacy().getCountryNpc().getName()));
		// i18nUtils.addParm("hp",
		// I18nPack.valueOf(targetCountry.getDiplomacy().getNpcHpPercentage() +
		// "%"));
		// ChatManager.getInstance().sendSystem(11003, i18nUtils, null,
		// hiter.getCountry());
	}

	/** 电视 : 广播我国战士正在浴血奋战国旗 */
	public void broadcastCallByFightingFlag(Player hiter, Country targetCountry) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("10301");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(targetCountry.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(targetCountry.getCountryFlag().getCountryNpc().getName()));
		// i18nUtils.addParm("hp",
		// I18nPack.valueOf(targetCountry.getCountryFlag().getNpcHpPercentage()
		// + "%"));
		// ChatManager.getInstance().sendSystem(11003, i18nUtils, null,
		// hiter.getCountry());
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 电视 : 本国国旗快被砍死,召集本国玩家 */
	public void broadcastCallByHiterFlag(Player hiter, Country selfCountry, int hp) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("10302");
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(selfCountry.getCountryFlag().getCountryNpc().getName()));
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(hiter.getCountry().getName()));
		// i18nUtils.addParm("hiter", I18nPack.valueOf(hiter.getName()));
		// i18nUtils.addParm("hp", I18nPack.valueOf(hp + "%"));
		// ChatManager.getInstance().sendSystem(11003, i18nUtils, null,
		// selfCountry);
	}

	/** 电视 : 本国大臣快被砍死,召集本国玩家 */
	public void broadcastCallByHiterDip(Player hiter, Country selfCountry, int hp) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("10304");
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(selfCountry.getDiplomacy().getCountryNpc().getName()));
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(hiter.getCountry().getName()));
		// i18nUtils.addParm("hiter", I18nPack.valueOf(hiter.getName()));
		// i18nUtils.addParm("hp", I18nPack.valueOf(hp + "%"));
		// ChatManager.getInstance().sendSystem(11003, i18nUtils, null,
		// selfCountry);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 电视 : 大臣死亡播放 */
	public void broadcastDipDie(int mostDamageCountry, Country dieCountry) {
		Country hiterCountry = CountryManager.getInstance().getCountryByValue(mostDamageCountry);
		I18nUtils i18nUtils = I18nUtils.valueOf("10303");
		i18nUtils.addParm("dietCountry", I18nPack.valueOf(dieCountry.getName()));
		i18nUtils.addParm("npc", I18nPack.valueOf(dieCountry.getDiplomacy().getCountryNpc().getName()));
		i18nUtils.addParm("hitCountry", I18nPack.valueOf(hiterCountry.getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null, hiterCountry);

		I18nUtils utils = I18nUtils.valueOf("301019", i18nUtils);
		ChatManager.getInstance().sendSystem(0, utils, null);
	}

	/** 电视 : 国旗死亡播放 */
	public void broadcastFlagDie(int mostDamageCountry, Country dieCountry) {
		Country hiterCountry = CountryManager.getInstance().getCountryByValue(mostDamageCountry);
		I18nUtils i18nUtils = I18nUtils.valueOf("10303");
		i18nUtils.addParm("dietCountry", I18nPack.valueOf(dieCountry.getName()));
		i18nUtils.addParm("npc", I18nPack.valueOf(dieCountry.getCountryFlag().getCountryNpc().getName()));
		i18nUtils.addParm("hitCountry", I18nPack.valueOf(hiterCountry.getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null, hiterCountry);

		I18nUtils utils = I18nUtils.valueOf("301019", i18nUtils);
		ChatManager.getInstance().sendSystem(0, utils, null);

		// I18nUtils debuffUtils = I18nUtils.valueOf("10307");
		// debuffUtils.addParm("npc",
		// I18nPack.valueOf(dieCountry.getCountryFlag().getCountryNpc().getName()));
		// debuffUtils.addParm("targetCountry",
		// I18nPack.valueOf(hiter.getCountry().getName()));
		// ChatManager.getInstance().sendSystem(11003, debuffUtils, null,
		// dieCountry);

		// I18nUtils debuffChat = I18nUtils.valueOf("301020", debuffUtils);
		// ChatManager.getInstance().sendSystem(6, debuffChat, null,
		// dieCountry);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 聊天

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 聊天 : 集结砍大臣 */
	public void broadcastCallDiplomacy(Player player, Country targetCountry) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301010");
		// i18nUtils.addParm("hiter", I18nPack.valueOf(player.getName()));
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(targetCountry.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(targetCountry.getDiplomacy().getCountryNpc().getName()));
		// i18nUtils.addParm("mapId",
		// I18nPack.valueOf(getMapId(player.getCountry(), FLAG_DIP)));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// player.getCountry());
	}

	/** 聊天 : 集结砍国旗 */
	public void broadcastCallCountryFlag(Player player, Country targetCountry) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301010");
		// i18nUtils.addParm("hiter", I18nPack.valueOf(player.getName()));
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(targetCountry.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(targetCountry.getCountryFlag().getCountryNpc().getName()));
		// i18nUtils.addParm("mapId",
		// I18nPack.valueOf(getMapId(player.getCountry(), FLAG_FLAG)));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// player.getCountry());
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 聊天 : 本国大臣被砍,求救 */
	public void broadcastCallDiplomacyHelp(Player hiter, Country selfCaountry) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301011");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(hiter.getCountry().getName()));
		// i18nUtils.addParm("hiter", I18nPack.valueOf(hiter.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(selfCaountry.getDiplomacy().getCountryNpc().getName()));
		// i18nUtils.addParm("transportId",
		// (I18nPack.valueOf(getFlyId(selfCaountry, FLAG_DIP))));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// selfCaountry);
	}

	/** 聊天 : 本国国旗被砍,求救 */
	public void broadcastCallCountryFlagHelp(Player player, Country selfCaountry) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301014");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(player.getCountry().getName()));
		// i18nUtils.addParm("hiter", I18nPack.valueOf(player.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(selfCaountry.getCountryFlag().getCountryNpc().getName()));
		// i18nUtils.addParm("transportId",
		// (I18nPack.valueOf(getFlyId(selfCaountry, FLAG_FLAG))));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// selfCaountry);
	}

	// ///////////////////////////////////////////////////////////////////////////
	/** 聊天 : 对方国旗快被砍死,召集本国玩家 */
	public void broadcastCallByHiterFlagChat(Player hiter, Country targetCountry, int hp) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301015");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(targetCountry.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(targetCountry.getCountryFlag().getCountryNpc().getName()));
		// i18nUtils.addParm("hp", I18nPack.valueOf(hp + "%"));
		// i18nUtils.addParm("mapId", I18nPack.valueOf(getMapId(targetCountry,
		// FLAG_FLAG)));
		// i18nUtils.addParm("x", I18nPack.valueOf(getMapX(targetCountry,
		// FLAG_FLAG)));
		// i18nUtils.addParm("y", I18nPack.valueOf(getMapY(targetCountry,
		// FLAG_FLAG)));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// hiter.getCountry());
	}

	/** 聊天 :对方大臣快被砍死,召集本国玩家 */
	public void broadcastCallByHiterDipChat(Player hiter, Country targetCountry, int hp) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301012");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(targetCountry.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(targetCountry.getDiplomacy().getCountryNpc().getName()));
		// i18nUtils.addParm("hp", I18nPack.valueOf(hp + "%"));
		// i18nUtils.addParm("mapId", I18nPack.valueOf(getMapId(targetCountry,
		// FLAG_DIP)));
		// i18nUtils.addParm("x", I18nPack.valueOf(getMapX(targetCountry,
		// FLAG_DIP)));
		// i18nUtils.addParm("y", I18nPack.valueOf(getMapY(targetCountry,
		// FLAG_DIP)));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// hiter.getCountry());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 聊天 : 本国国旗危险,hp播放 */
	public void broadcastCallCountryFlagDanger(Player hiter, Country selfCaountry, int hp) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301016");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(hiter.getCountry().getName()));
		// i18nUtils.addParm("hiter", I18nPack.valueOf(hiter.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(selfCaountry.getCountryFlag().getCountryNpc().getName()));
		// i18nUtils.addParm("hp", I18nPack.valueOf(hp + "%"));
		// i18nUtils.addParm("transportId",
		// (I18nPack.valueOf(getFlyId(selfCaountry, FLAG_FLAG))));
		// i18nUtils.addParm("x", I18nPack.valueOf(getMapX(selfCaountry,
		// FLAG_FLAG)));
		// i18nUtils.addParm("y", I18nPack.valueOf(getMapY(selfCaountry,
		// FLAG_FLAG)));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// selfCaountry);
	}

	/** 聊天 : 本国大臣危险,hp播放 */
	public void broadcastCallDiplomacyDanger(Player hiter, Country selfCaountry, int hp) {
		// I18nUtils i18nUtils = I18nUtils.valueOf("301013");
		// i18nUtils.addParm("targetCountry",
		// I18nPack.valueOf(hiter.getCountry().getName()));
		// i18nUtils.addParm("hiter", I18nPack.valueOf(hiter.getName()));
		// i18nUtils.addParm("npc",
		// I18nPack.valueOf(selfCaountry.getDiplomacy().getCountryNpc().getName()));
		// i18nUtils.addParm("hp", I18nPack.valueOf(hp + "%"));
		// i18nUtils.addParm("transportId",
		// I18nPack.valueOf(getFlyId(selfCaountry, FLAG_DIP)));
		// ChatManager.getInstance().sendSystem(6, i18nUtils, null,
		// selfCaountry);
	}

	/*
	 * private String getMapId(Country country, int flag) { return
	 * get_MapId_X_Y_FlyId(country, flag).get("mapId"); }
	 * 
	 * private String getMapX(Country country, int flag) { return
	 * get_MapId_X_Y_FlyId(country, flag).get("x"); }
	 * 
	 * private String getMapY(Country country, int flag) { return
	 * get_MapId_X_Y_FlyId(country, flag).get("y"); }
	 * 
	 * private String getFlyId(Country country, int flag) { return
	 * get_MapId_X_Y_FlyId(country, flag).get("flyId"); }
	 *//**
	 * 
	 * @param country
	 * @param flag
	 *            1.大臣 2.国旗
	 * @return [mapId , x , y, flyId]
	 */
	/*
	 * private Map<String, String> get_MapId_X_Y_FlyId(Country country, int
	 * flag) { Map<String, String> positionMap = null; if (flag == FLAG_DIP) {
	 * positionMap =
	 * ConfigValueManager.getInstance().COUNTRY_DIP_POS.getValue().
	 * get(country.getId().getValue() + ""); } else if (flag == FLAG_FLAG) {
	 * positionMap =
	 * ConfigValueManager.getInstance().COUNTRY_FLAG_POS.getValue()
	 * .get(country.getId().getValue() + ""); } return positionMap; }
	 */

	/** 大臣 */
	private static final byte FLAG_DIP = 1;
	/** 国旗 */
	private static final byte FLAG_FLAG = 2;
}
