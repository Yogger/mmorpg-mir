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
 * 等级奖励
 * 
 * @author Kuang Hao
 * @since v1.0 2014-12-9
 * 
 */
@Component
public class LevelRewardsProvider extends RewardProvider {
	@Autowired
	private PlayerService playerService;

	/** 开始等级 */
	private String START_LEVEL = "START_LEVEL";
	/** 结束等级 */
	private String END_LEVEL = "END_LEVEL";

	@Override
	public RewardType getType() {
		return RewardType.LEVEL;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		PlayerExpUpdate playerExpUpdate = null;
		if (player.isMaxLevel()) {
			return;
		}
		int startLevel = Integer.valueOf(rewardItem.getParms().get(START_LEVEL));
		int endLevel = Integer.valueOf(rewardItem.getParms().get(END_LEVEL));
		if (startLevel <= player.getLevel() && player.getLevel() <= endLevel) {
			playerExpUpdate = playerService.levelUpExp(player);
		} else {
			int exp = rewardItem.getAmount();
			playerExpUpdate = playerService.addExp(player, exp, !module.isMonsterDrop(), module);
		}

		// 通知前端
		PacketSendUtility.sendPacket(
				player,
				SM_PlayerExpUpdate.valueOf(playerExpUpdate.getLevel(), playerExpUpdate.getAddExp(),
						playerExpUpdate.getExp()));

	}
}
