package com.mmorpg.mir.model.country.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

/**
 * 国家金钱奖励
 * 
 * @author Kuang Hao
 * @since v1.0 2012-3-6
 * 
 */
@Component
public class CountryCurrencyRewardsProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.COUNTRY_CURRENCY;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		CoppersType type = CoppersType.valueOf(Integer.valueOf(rewardItem.getCode()));
		player.getCountry().getCoppers().add(type, Long.valueOf(rewardItem.getAmount()));
	}
}
