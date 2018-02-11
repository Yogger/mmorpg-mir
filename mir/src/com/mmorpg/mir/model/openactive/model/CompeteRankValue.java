package com.mmorpg.mir.model.openactive.model;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.resource.OldOpenActiveCompeteResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveCompeteResource;

/**
 * 开服竞技对应排行榜
 */
public enum CompeteRankValue {
	/** 消费排行榜 **/
	CONSUME_RANK(25),

	/** 等级排行榜 **/
	LEVEL_RANK(26),

	/** 坐骑排行榜 **/
	HORSE_RANK(29),

	/** 全身强化排行榜 **/
	ENHANCE_RANK(30),

	/** 神兵排行榜 **/
	ARTIFACT_RANK(31),

	/** 军衔排行榜 **/
	MILITARY_RANK(32),

	/** 战力排行榜 **/
	FIGHTPOWER_RANK(33),
	
	/** 英魂排行榜 **/
	SOUL_RANK(34),
	
	/** 老服的英魂排行榜 **/
	OLD_SOUL_RANK(35),
	
	/** 国家英雄排行榜 **/
	ACTIVITY_COUNTRY_HERO(38);

	public static CompeteRankValue valueOf(int rankType) {
		for (CompeteRankValue t : CompeteRankValue.values()) {
			if (t.rankTypeValue == rankType) {
				return t;
			}
		}
		throw new RuntimeException(" no match type of CompeteRankValue[" + rankType + "]");
	}

	public static CompeteRankValue findRank(int rankType) {
		for (CompeteRankValue t : CompeteRankValue.values()) {
			if (t.rankTypeValue == rankType) {
				return t;
			}
		}
		return null;
	}

	private final int rankTypeValue;

	CompeteRankValue(int v) {
		this.rankTypeValue = v;
	}

	public int getRankTypeValue() {
		return rankTypeValue;
	}

	public ArrayList<String> getCanRewardCount(Player player) {
		ArrayList<String> ret = New.arrayList();
		for (OpenActiveCompeteResource res : OpenActiveConfig.getInstance().getSpecifiedRankTypeResources(
				getRankTypeValue())) {
			if (!player.getOpenActive().getCompeteRankActivity(getRankTypeValue()).getRewarded().contains(res.getId())
					&& res.getRecieveConditions().verify(player)) {
				ret.add(res.getId());
			}
		}
		return ret;
	}
	
	public ArrayList<String> getOldCanRewardCount(Player player) {
		ArrayList<String> ret = New.arrayList();
		for (OldOpenActiveCompeteResource res : OpenActiveConfig.getInstance().getOldSpecifiedRankTypeResources(
				getRankTypeValue())) {
			if (!player.getOpenActive().getCompeteRankActivity(getRankTypeValue()).getRewarded().contains(res.getId())
					&& res.getRecieveConditions().verify(player)) {
				ret.add(res.getId());
			}
		}
		return ret;
	}
}
