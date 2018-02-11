package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class RewardTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.REWARD;
	}

	@Autowired
	private RewardManager rewardManager;

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		String rewardId = resource.getKeys().get(TriggerContextKey.REWARDID);
		rewardManager.grantReward(player, rewardId, ModuleInfo.valueOf(ModuleType.TRIGGER, SubModuleType.TRIGGER_REWARD, resource.getId()));
	}

}
