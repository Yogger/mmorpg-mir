package com.mmorpg.mir.model.reward.model;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.manager.RewardManager;

/**
 * 奖励发放器
 * 
 * @author Kuang Hao
 * @since v1.0 2013-1-24
 * 
 */
public abstract class RewardProvider {

	@Autowired
	private RewardManager rewardManager;

	@PostConstruct
	public void init() {
		rewardManager.registerProvider(this);
	}

	public abstract RewardType getType();

	/**
	 * 获取奖励内容
	 * 
	 * @param reward
	 * @return
	 */
	public abstract void withdraw(Player player, RewardItem rewardItem, ModuleInfo module);

}
