package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.windforce.common.utility.New;

public class EquipEnhanceActive implements CompeteRankActivity{
	
	private int maxCount;
	/** 已经领取的经验礼包 */
	private HashSet<String> rewarded = New.hashSet();

	public static EquipEnhanceActive valueOf() {
		EquipEnhanceActive result = new EquipEnhanceActive();
		result.rewarded = New.hashSet();
		return result;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	
	@JsonIgnore
	public int updateMaxCount(Player player) {
		int allEnhanceLevel = 0;
		for (Equipment equip: player.getEquipmentStorage().getEquipments()) {
			if (equip != null) {
				allEnhanceLevel += equip.getEnhanceLevel();
			}
		}
		if (allEnhanceLevel > maxCount) {
			maxCount = allEnhanceLevel;
			return maxCount;
		}
		return 0;
	}

	@JsonIgnore
	public int getCompeteValue() {
		return maxCount;
	}

	@JsonIgnore
	public int getCompeteRankTypeValue() {
		return CompeteRankValue.ENHANCE_RANK.getRankTypeValue();
	}

}
