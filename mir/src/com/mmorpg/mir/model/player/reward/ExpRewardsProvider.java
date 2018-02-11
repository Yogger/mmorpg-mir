package com.mmorpg.mir.model.player.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.model.PlayerExpUpdate;
import com.mmorpg.mir.model.player.packet.SM_PlayerExpUpdate;
import com.mmorpg.mir.model.player.service.PlayerService;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

/**
 * 经验奖励
 * 
 * @author Kuang Hao
 * @since v1.0 2012-3-6
 * 
 */
@Component
public class ExpRewardsProvider extends RewardProvider {
	@Autowired
	private PlayerService playerService;

	@Override
	public RewardType getType() {
		return RewardType.EXP;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		if (rewardItem.getAmount() < 0) {
			throw new RuntimeException(String.format("经验增加负值[%s]", rewardItem.getAmount()));
		}
		PlayerExpUpdate playerExpUpdate = playerService.addExp(player, (int) (rewardItem.getAmount() * player
				.getAddication().getRate()), !module.isMonsterDrop(), module);
		// 通知前端
		PacketSendUtility.sendPacket(
				player,
				SM_PlayerExpUpdate.valueOf(playerExpUpdate.getLevel(), playerExpUpdate.getAddExp(),
						playerExpUpdate.getExp()));

	}

}
