package com.mmorpg.mir.model.country.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.country.packet.SM_Country_Shop_Buy;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;

public class CountryShop {

	private List<String> items;

	private Map<Long, Map<String, Integer>> buyHis;
	
	private int level = 1;

	private long lastRefreshTime;

	public static CountryShop valueOf() {
		CountryShop countryShop = new CountryShop();
		countryShop.buyHis = New.hashMap();
		return countryShop;
	}
	
	@JsonIgnore
	public void upgradeCountryShop() {
		this.level++;
	}

	@JsonIgnore
	public void addCount(Player player, String id, int count) {
		if (!buyHis.containsKey(player.getObjectId())) {
			buyHis.put(player.getObjectId(), new HashMap<String, Integer>());
		}
		Map<String, Integer> buyed = buyHis.get(player.getObjectId());
		buyed.put(id, (buyed.containsKey(id) ? buyed.get(id) : 0) + count);
		PacketSendUtility.sendPacket(player, SM_Country_Shop_Buy.valueOf(buyed));
	}

	@JsonIgnore
	public int getCount(long playerId, String id) {
		if (!buyHis.containsKey(playerId)) {
			return 0;
		}
		if (!buyHis.get(playerId).containsKey(id)) {
			return 0;
		}
		return buyHis.get(playerId).get(id);
	}

	public void reset(long now, List<String> items) {
		buyHis.clear();
		lastRefreshTime = now;
		this.items = items;
	}

	public List<String> getItems() {
		return items;
	}

	@Deprecated
	@JsonIgnore
	public boolean isDeprecated(int cd) {
		if ((System.currentTimeMillis() - cd) > lastRefreshTime) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public long getElapsedTime() {
		return DateUtils.getNextDayFirstTime(new Date()).getTime();
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public Map<Long, Map<String, Integer>> getBuyHis() {
		return buyHis;
	}

	public void setBuyHis(Map<Long, Map<String, Integer>> buyHis) {
		this.buyHis = buyHis;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
