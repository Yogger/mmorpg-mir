package com.mmorpg.mir.model.welfare.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Active_Open_Panel;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;

@Component
public class ActiveValueRewardsProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.ACTIVEVALUE;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		player.getWelfare().getActiveValue().addValue(rewardItem.getAmount());
		PacketSendUtility.sendPacket(player, SM_Welfare_Active_Open_Panel.valueOf(player));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player,
				SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
	}

}
