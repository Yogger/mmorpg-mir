package com.mmorpg.mir.model.commonactivity.packet;

import java.util.ArrayList;
import java.util.HashMap;

import com.mmorpg.mir.model.commonactivity.model.CommonMarcoShop;
import com.mmorpg.mir.model.commonactivity.model.CommonMarcoShopGood;

public class SM_CommonMarcoShop_Refresh {
	private String activityName;
	private boolean systemRefresh;
	private HashMap<String, Integer> restrictGoods;
	private ArrayList<CommonMarcoShopGood> goods;
	private long lastRefreshTime;

	public static SM_CommonMarcoShop_Refresh valueOf(boolean systemRefresh, CommonMarcoShop shop) {
		SM_CommonMarcoShop_Refresh result = new SM_CommonMarcoShop_Refresh();
		result.activityName = shop.getActivityName();
		result.systemRefresh = systemRefresh;
		result.restrictGoods = new HashMap<String, Integer>(shop.getRestrictGoods());
		result.goods = new ArrayList<CommonMarcoShopGood>(shop.getGoods());
		result.lastRefreshTime = shop.getLastRefreshTime();
		return result;
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

	public ArrayList<CommonMarcoShopGood> getGoods() {
		return goods;
	}

	public void setGoods(ArrayList<CommonMarcoShopGood> goods) {
		this.goods = goods;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
}
