package com.mmorpg.mir.model.commonactivity.packet;

import java.util.ArrayList;
import java.util.HashMap;

import com.mmorpg.mir.model.commonactivity.model.CommonMarcoShopGood;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_CommonMarcoShop_Query_Info {
	private String activityName;

	private HashMap<String, Integer> restrictGoods;

	private ArrayList<CommonMarcoShopGood> goods;

	private long lastRefreshTime;

	public static SM_CommonMarcoShop_Query_Info valueOf(Player player, String activityName) {
		SM_CommonMarcoShop_Query_Info result = new SM_CommonMarcoShop_Query_Info();
		result.activityName = activityName;
		result.goods = new ArrayList<CommonMarcoShopGood>(player.getCommonActivityPool().getCommonMarcoShop()
				.get(activityName).getGoods());
		result.restrictGoods = new HashMap<String, Integer>(player.getCommonActivityPool().getCommonMarcoShop()
				.get(activityName).getRestrictGoods());
		result.lastRefreshTime = player.getCommonActivityPool().getCommonMarcoShop().get(activityName)
				.getLastRefreshTime();
		return result;
	}

	public String getActivityName() {
		return activityName;
	}

	public HashMap<String, Integer> getRestrictGoods() {
		return restrictGoods;
	}

	public ArrayList<CommonMarcoShopGood> getGoods() {
		return goods;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setRestrictGoods(HashMap<String, Integer> restrictGoods) {
		this.restrictGoods = restrictGoods;
	}

	public void setGoods(ArrayList<CommonMarcoShopGood> goods) {
		this.goods = goods;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

}
