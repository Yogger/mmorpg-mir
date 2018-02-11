package com.mmorpg.mir.model.combatspirit.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.combatspirit.packet.SM_CombatSpirit_Reward;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class CombatSpiritGrowProvider extends RewardProvider {

	public static final String SPECIAL = "SPECIAL";
	
	@Override
	public RewardType getType() {
		return RewardType.COMBAT_SPIRIT;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		CombatSpiritType type = CombatSpiritType.valueOf(rewardItem.getCode());
		CombatSpirit spirit = player.getCombatSpiritStorage().getCombatSpiritCollection().get(type.getValue());
		spirit.notifyUpgrade(player, rewardItem.getAmount());
		spirit.addGrowUpValue(rewardItem.getAmount(), rewardItem.getParms() != null && rewardItem.getParms().containsKey(SPECIAL));
		PacketSendUtility.sendPacket(player, SM_CombatSpirit_Reward.valueOf(spirit, rewardItem.getAmount()));
	}
}
