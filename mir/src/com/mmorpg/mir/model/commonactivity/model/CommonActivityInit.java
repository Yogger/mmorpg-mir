package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class CommonActivityInit extends ModuleHandle {
	@Override
	public ModuleKey getModule() {
		return ModuleKey.COMMON_ACTIVITY;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getActivityJson() == null) {
			ent.setActivityJson(JsonUtils.object2String(CommonActivityPool.valueOf()));
		}
		Player player = ent.getPlayer();
		if (player.getCommonActivityPool() == null) {
			CommonActivityPool activity = JsonUtils.string2Object(ent.getActivityJson(), CommonActivityPool.class);
			if (activity.getIdentifyTreasures() == null) {
				activity.setIdentifyTreasures(new HashMap<String, CommonIdentifyTreasure>());
			}

			if (activity.getCommonMarcoShop() == null) {
				activity.setCommonMarcoShop(new HashMap<String, CommonMarcoShop>());
			}

			if (activity.getFirstPays() == null) {
				activity.setFirstPays(new HashMap<String, CommonFirstPay>());
			}
			if (activity.getRedPacketActives() == null) {
				activity.setRedPacketActives(new HashMap<String, CommonRedPack>());
			}

			if (activity.getTreasurueActives() == null) {
				activity.setTreasurueActives(new HashMap<String, CommonTreasureActive>());
			}
			
			if (activity.getRecollectActives() == null) {
				activity.setRecollectActives(new HashMap<String, CommonRecollectActive>());
			}
			if(activity.getLuckyDraw() == null){
				activity.setLuckyDraw(new LuckyDraw());
			}
			if(activity.getGoldTreasurys() == null){
				activity.setGoldTreasurys(new HashMap<String, CommonGoldTreasury>());
			}
			if(activity.getConsumeGifts() == null){
				activity.setConsumeGifts(new HashMap<String, CommonConsumeGift>());
			}
			player.setCommonActivityPool(activity);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getCommonActivityPool() != null) {
			ent.setActivityJson(JsonUtils.object2String(player.getCommonActivityPool()));
		}
	}
}
