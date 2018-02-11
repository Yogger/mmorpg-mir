package com.mmorpg.mir.model.footprint.reward;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class FootprintRewardProvider extends RewardProvider {
	
	private static final String FOOTSTAR = "FOOTSTAR";

	@Override
	public RewardType getType() {
		return RewardType.FOOTPRINT;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Map<String, String> parms = rewardItem.getParms();
		String star = null;
		if (parms != null) {
			star = parms.get(FOOTSTAR);
		}
		player.getFootprintPool().reward(player, Integer.valueOf(rewardItem.getCode()), rewardItem.getAmount(), star);
	}

}
