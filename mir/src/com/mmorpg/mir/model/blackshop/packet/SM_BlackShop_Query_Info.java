package com.mmorpg.mir.model.blackshop.packet;

import java.util.ArrayList;
import java.util.HashMap;

import com.mmorpg.mir.model.blackshop.model.BlackShopGood;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_BlackShop_Query_Info {
	private HashMap<String, Integer> restrictGoods;

	private ArrayList<BlackShopGood> goods;

	private long lastRefreshTime;

	public static SM_BlackShop_Query_Info valueOf(Player player) {
		SM_BlackShop_Query_Info result = new SM_BlackShop_Query_Info();
		result.goods = new ArrayList<BlackShopGood>(player.getBlackShop().getGoods());
		result.restrictGoods = new HashMap<String, Integer>(player.getBlackShop().getRestrictGoods());
		result.lastRefreshTime = player.getBlackShop().getLastRefreshTime();
		return result;
	}

	public ArrayList<BlackShopGood> getGoods() {
		return goods;
	}

	public void setGoods(ArrayList<BlackShopGood> goods) {
		this.goods = goods;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public HashMap<String, Integer> getRestrictGoods() {
		return restrictGoods;
	}

	public void setRestrictGoods(HashMap<String, Integer> restrictGoods) {
		this.restrictGoods = restrictGoods;
	}

}
