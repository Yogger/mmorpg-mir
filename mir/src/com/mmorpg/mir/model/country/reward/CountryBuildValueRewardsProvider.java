package com.mmorpg.mir.model.country.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class CountryBuildValueRewardsProvider extends RewardProvider {

	@Autowired
	private CountryManager countryManager;

	@Override
	public RewardType getType() {
		return RewardType.COUNTRY_BUILDVALUE;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		/*
		 * 玩家使用建设令可获得：建设值。建设值通过公式计算所得，建设值=基础值+特殊加成值 
		 * 基础值：首先由数值确定一个区间，在这个区间内，纯随机取一个具体的数值，这个数值为基础值 
		 * 特殊加成值：基础值*（1+落后科技加成）。其中落后科技加成
		 * =[（当前全服最高国家科技建设值-本国国家科技建设值）/当前全服最高国家科技建设值]*系数
		 */
		countryManager.increaseBuildValue(player, rewardItem.getAmount());
	}
}
