package com.mmorpg.mir.model.horse.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.SpringComponentStation;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;
import com.mmorpg.mir.model.horse.resource.HorseResource;
import com.mmorpg.mir.model.horse.service.HorseService;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

/**
 * 坐骑卡道具
 * 
 * @author 37.com
 * 
 */
@Component
public class HorseItemRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.HORSEITEM;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		// 分割的阶数
		// int divideLevel = Integer.parseInt(rewardItem.getCode());
		// Horse horse = player.getHorse();
		// HorseResource resource = horse.getResource();
		// if (resource.getCount() == 0) {
		// // 已到最高等级
		// return;
		// }
		// resource = HorseManager.getInstance().getHorseResource(divideLevel);
		// HorseService service = SpringComponentStation.getHorseService();
		// if (horse.getGrade() < divideLevel) {
		// while (horse.getGrade() < divideLevel) {
		// service.addGrade(player);
		// }
		// service.flushHorse(player, true, true);
		// } else {
		// // 增加的祝福值
		// int blessValue = rewardItem.getAmount();
		// boolean isUpLevel = false;
		// horse.addBlessValue(blessValue);
		// if (horse.isMaxBless()) {
		// service.addGrade(player);
		// isUpLevel = true;
		// }else{
		// horse.addUpSum(1);
		// }
		// service.flushHorse(player, isUpLevel, true);
		// }
		// SM_HorseUpdate sm = player.getHorse().createVO();
		// PacketSendUtility.sendPacket(player, sm);
	}
}
