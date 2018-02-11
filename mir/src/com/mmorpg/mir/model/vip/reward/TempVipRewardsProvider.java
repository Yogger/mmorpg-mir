package com.mmorpg.mir.model.vip.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.vip.packet.SM_Vip_Add_Temp;
import com.windforce.common.utility.DateUtils;

/**
 * 等级奖励
 * 
 * @author Kuang Hao
 * @since v1.0 2014-12-9
 * 
 */
@Component
public class TempVipRewardsProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.TEMPLE_VIP;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		int min = rewardItem.getAmount();
		player.getVip().addNoUseTempVipTime(min * DateUtils.MILLIS_PER_MINUTE);
		// 通知前端
		PacketSendUtility.sendPacket(player, new SM_Vip_Add_Temp());
	}
}
