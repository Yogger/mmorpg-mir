package com.mmorpg.mir.model.blackshop.packet;

import java.util.ArrayList;
import java.util.HashMap;

import com.mmorpg.mir.model.blackshop.model.BlackShop;
import com.mmorpg.mir.model.blackshop.model.BlackShopGood;

public class SM_BlackShop_Refresh {
	private boolean systemRefresh;
	private HashMap<String, Integer> restrictGoods;
	private ArrayList<BlackShopGood> goods;
	private long lastRefreshTime;

	public static SM_BlackShop_Refresh valueOf(boolean systemRefresh, BlackShop shop) {
		SM_BlackShop_Refresh result = new SM_BlackShop_Refresh();
		result.systemRefresh = systemRefresh;
		result.restrictGoods = new HashMap<String, Integer>(shop.getRestrictGoods());
		result.goods = new ArrayList<BlackShopGood>(shop.getGoods());
		result.lastRefreshTime = shop.getLastRefreshTime();
		return result;
	}

	public ArrayList<BlackShopGood> getGoods() {
		return goods;
	}

	public void setGoods(ArrayList<BlackShopGood> goods) {
		this.goods = goods;
	}

	public boolean isSystemRefresh() {
		return systemRefresh;
	}

	public void setSystemRefresh(boolean systemRefresh) {
		this.systemRefresh = systemRefresh;
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
