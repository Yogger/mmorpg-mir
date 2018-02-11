package com.mmorpg.mir.model.warship.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.warship.event.WarshipEvent;
import com.mmorpg.mir.model.warship.manager.WarshipManager;
import com.mmorpg.mir.model.warship.packet.SM_Warship_Refresh;
import com.mmorpg.mir.model.warship.packet.SM_Warship_Status;
import com.mmorpg.mir.model.warship.packet.SM_Warship_king;
import com.mmorpg.mir.model.warship.resource.WarshipResource;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.New;
import com.windforce.common.utility.RandomUtils;

@Component
public class WarshipServiceImpl implements WarshipService {
	@Autowired
	private WarshipManager warshipManager;

	@Autowired
	private KingOfWarManager kingOfWarManager;

	public void getWarshipStatus(Player player) {
		Player king = kingOfWarManager.getKingOfKing();
		long becomeTime = kingOfWarManager.getBecomeKingOfKingTime();
		int supportCount = kingOfWarManager.getKingSupportCount();
		int contemptCount = kingOfWarManager.getKingContemptCount();
		PacketSendUtility.sendPacket(player,
				SM_Warship_Status.valueOf(king, player.getWarship(), becomeTime, supportCount, contemptCount));
	}

	public void warShipKing(Player player, boolean isSupport) {
		WarshipResource resource = warshipManager.getWarshipResource(player.getWarship().getCurrentSelect());
		if (!resource.getWarshipCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		CoreActions actions = resource.getWarshipActions();
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.WARSHIP, SubModuleType.WARSHIP_ACT));

		if (isSupport) {
			kingOfWarManager.supportKing();
		} else {
			kingOfWarManager.contemptKing();
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserGroup());
		Map<String, Object> params = New.hashMap();
		params.put("LEVEL", player.getLevel());
		params.put("STANDARD_INCR", PlayerManager.getInstance().getStandardIncr(player));
		params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		player.getWarship().setCurrentSelect(0);
		Reward reward = RewardManager.getInstance().grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.WARSHIP, SubModuleType.WARSHIP_REWARD), params);
		PacketSendUtility.sendPacket(player, SM_Warship_king.valueOf(player.getWarship().warshipConsume(),
				kingOfWarManager.getKingSupportCount(), kingOfWarManager.getKingContemptCount()));

		warshipManager.logGain(player, reward);
		EventBusManager.getInstance().submit(WarshipEvent.valueOf(player.getObjectId()));
	}

	public void warShipRefreshReward(Player player, boolean isGold) {
		WarshipResource resource = warshipManager.getWarshipResource(player.getWarship().getCurrentSelect());
		if (!resource.getWarshipCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (player.getWarship().getCurrentSelect() >= WarshipResource.REWARD_ORANGE) {
			throw new ManagedException(ManagedErrorCode.WARSHIP_ALREADY_ORANGE);
		}

		if (isGold) {
			CoreActions goldActions = resource.getRefreshingGoldActions();
			goldActions.verify(player, true);
			goldActions.act(player, ModuleInfo.valueOf(ModuleType.WARSHIP, SubModuleType.WARSHIP_GOLD_REFRESH));
			player.getWarship().setCurrentSelect(WarshipResource.REWARD_ORANGE);
		} else {
			CoreActions actions = resource.getRefreshingActions();
			actions.verify(player, true);
			actions.act(player, ModuleInfo.valueOf(ModuleType.WARSHIP, SubModuleType.WARSHIP_REFRESH));
			double rate = resource.getRefreshSuccRate() / 10000.0;
			boolean firstWarship = (player.getWarship().getTotalWarshipCount() == 0);
			if (firstWarship || RandomUtils.isHit(rate)) {
				player.getWarship().setCurrentSelect(player.getWarship().getCurrentSelect() + 1);
			}
		}

		PacketSendUtility.sendPacket(player, SM_Warship_Refresh.valueOf(player.getWarship().getCurrentSelect()));
	}
}
