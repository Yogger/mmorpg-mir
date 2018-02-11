package com.mmorpg.mir.model.purse.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.CurrencyUtils;
import com.mmorpg.mir.model.purse.event.CurrencyRewardEvent;
import com.mmorpg.mir.model.purse.event.RechargeRewardEvent;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.windforce.common.event.core.EventBusManager;

/**
 * 金钱奖励
 * 
 * @author Kuang Hao
 * @since v1.0 2012-3-6
 * 
 */
@Component
public class CurrencyRewardsProvider extends RewardProvider {

	public static final String VIP_REWARD = "VIP";

	@Autowired
	private CurrencyUtils currencyUtils;
	@Autowired
	private PlayerManager playerManager;

	@Override
	public RewardType getType() {
		return RewardType.CURRENCY;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		int amount = rewardItem.getAmount();
		String vipReward = null;
		if (rewardItem.getParms() != null) {
			vipReward = rewardItem.getParms().get(VIP_REWARD);
		}
		CurrencyType currencyType = CurrencyType.valueOf(Integer.valueOf(rewardItem.getCode()));
		boolean isVipReward = ((vipReward != null) && vipReward.equals("true"));
		if (currencyType == CurrencyType.COPPER) {
			amount = (int) (amount * player.getAddication().getRate());
		} else if ((currencyType == CurrencyType.GOLD && isVipReward) || currencyType == CurrencyType.INTER) {
			player.getVip().compareAndSetMaxCharge(amount);
			player.getVip().addTotalCharge(amount);
			player.getVip().addGrowth(player.getObjectId(), rewardItem.getAmount());
			player.getVip().addGoldRechargeHistory(rewardItem.getAmount(), System.currentTimeMillis());
			EventBusManager.getInstance().submit(RechargeRewardEvent.valueOf(player.getObjectId(), amount));
		}

		currencyUtils.incomeByLog(player, currencyType, amount, module);
		EventBusManager.getInstance().submit(CurrencyRewardEvent.valueOf(player.getObjectId(), amount, currencyType));

		playerManager.updatePlayer(player);

		player.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_PURSE);
	}
}
