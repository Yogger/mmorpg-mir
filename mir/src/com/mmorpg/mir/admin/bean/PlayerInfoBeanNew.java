package com.mmorpg.mir.admin.bean;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.model.AccountStatus;
import com.mmorpg.mir.model.purse.model.PurseVO;

public class PlayerInfoBeanNew {
	private long guid;
	private String op;
	private String server;
	private String account;
	private String name;
	private int level;
	private long exp;
	private int mapId;
	private int country;
	private int role;
	private int x;
	private int y;
	private int currentHp;
	private int currentMp;
	private int currentDp;
	private Map<String, Long> currencies;
	private Map<String, Long> totalCurrencies;
	private boolean forbidChat;
	/** @see AccountStatus */
	private int accountStatus;
	private int vipLevel;
	private boolean gm;

	public long getGuid() {
		return guid;
	}

	public void setGuid(long guid) {
		this.guid = guid;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public Map<String, Long> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, Long> currencies) {
		this.currencies = currencies;
	}

	public Map<String, Long> getTotalCurrencies() {
		return totalCurrencies;
	}

	public void setTotalCurrencies(Map<String, Long> totalCurrencies) {
		this.totalCurrencies = totalCurrencies;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public int getCurrentMp() {
		return currentMp;
	}

	public void setCurrentMp(int currentMp) {
		this.currentMp = currentMp;
	}

	public int getCurrentDp() {
		return currentDp;
	}

	public void setCurrentDp(int currentDp) {
		this.currentDp = currentDp;
	}

	public static PlayerInfoBeanNew valueOf(Player player, boolean forbidChat, boolean isBan) {
		PlayerInfoBeanNew bean = new PlayerInfoBeanNew();
		bean.guid = player.getObjectId();
		bean.op = player.getPlayerEnt().getOp();
		bean.server = player.getPlayerEnt().getServer();
		bean.account = player.getPlayerEnt().getAccountName();
		bean.name = player.getName();
		bean.level = player.getLevel();
		bean.exp = player.getPlayerEnt().getExp();
		PurseVO vo = player.getPurse().creatPurseVO();
		bean.currencies = vo.getCurrencies();
		bean.totalCurrencies = vo.getTotalCurrencies();
		bean.country = player.getCountryValue();
		bean.role = player.getRole();
		if (player.getPosition() != null) {
			bean.x = player.getX();
			bean.y = player.getY();
			bean.mapId = player.getMapId();
		}
		bean.currentHp = (int) player.getLifeStats().getCurrentHp();
		bean.currentMp = (int) player.getLifeStats().getCurrentMp();
		bean.currentDp = (int) player.getLifeStats().getCurrentDp();
		bean.forbidChat = forbidChat;
		if (isBan) {
			bean.accountStatus = AccountStatus.BAN.value();
		} else {
			bean.accountStatus = player.getPlayerEnt().getStatus();
		}
		bean.vipLevel = player.getVip().getLevel();
		bean.gm = player.getOperatorPool().getGmPrivilege().isGm();
		return bean;
	}

	public boolean isForbidChat() {
		return forbidChat;
	}

	public void setForbidChat(boolean forbidChat) {
		this.forbidChat = forbidChat;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	public boolean isGm() {
		return gm;
	}

	public void setGm(boolean gm) {
		this.gm = gm;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

}
