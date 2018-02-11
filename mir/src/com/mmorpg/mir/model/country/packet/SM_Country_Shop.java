package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.country.model.CountryShop;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Country_Shop {
	private ArrayList<String> items;
	private Map<String, Integer> buyHis;
	private long eplaseTime;
	private int level;
	private long shopExp;

	public static SM_Country_Shop valueOf(Player player, CountryShop countryShop) {
		SM_Country_Shop sm = new SM_Country_Shop();
		sm.setItems((ArrayList<String>) countryShop.getItems());
		sm.buyHis = countryShop.getBuyHis().get(player.getObjectId());
		sm.eplaseTime = countryShop.getElapsedTime();
		sm.level = countryShop.getLevel();
		sm.shopExp = player.getCountry().getCoppers().getValue(CoppersType.SHOP_EXP);
		return sm;
	}

	public List<String> getItems() {
		return items;
	}

	public Map<String, Integer> getBuyHis() {
		return buyHis;
	}

	public void setBuyHis(Map<String, Integer> buyHis) {
		this.buyHis = buyHis;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}

	public long getEplaseTime() {
		return eplaseTime;
	}

	public void setEplaseTime(long eplaseTime) {
		this.eplaseTime = eplaseTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getShopExp() {
		return shopExp;
	}

	public void setShopExp(long shopExp) {
		this.shopExp = shopExp;
	}

}
