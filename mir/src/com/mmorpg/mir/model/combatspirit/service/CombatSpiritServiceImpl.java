package com.mmorpg.mir.model.combatspirit.service;

import java.util.List;
import java.util.Map;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.event.CombatSpiritUpEvent;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.combatspirit.packet.SM_CombatSpirit_Reward;
import com.mmorpg.mir.model.combatspirit.packet.SM_Query_CombatSpirit;
import com.mmorpg.mir.model.combatspirit.packet.SM_Upgrade_CombatSpirit;
import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.combatspirit.reward.CombatSpiritGrowProvider;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.anno.Static;

@Component
public class CombatSpiritServiceImpl implements CombatSpiritService {

	@Autowired
	private CombatSpiritManager manager;

	@Static("COMBATSPRIT:PROTECTURE_VALUE_ADD_COND")
	public ConfigValue<String[]> PROTECTURE_VALUE_ADD_COND;

	@Static("COMBATSPIRIT:PROTECTRUNE_INCREMENT")
	public ConfigValue<Integer> PROTECTRUNE_INCREMENT;

	@Static("COMBATSPIRIT:PROTECTURE_DAILY_LIMIT")
	public ConfigValue<Integer> PROTECTURE_DAILY_LIMIT;

	@Static("COMBATSPIRIT:SHORT_TIME_NO_ADD")
	private ConfigValue<Integer> SHORT_TIME_NO_ADD;

	@Static("COMBATSPIRIT:PROTECTURE_ADD_CD")
	private ConfigValue<Integer> PROTECTURE_ADD_CD;

	@Static("COMBATSPRIT:ITEM_ACQUIRE_CHOOSERGROUP")
	private ConfigValue<String> ITEM_ACQUIRE_CHOOSERGROUP;

	private CoreConditions PROTECTURE_ACQUIRE_CONDS;

	public CoreConditions getProctureAcquireConds() {
		if (PROTECTURE_ACQUIRE_CONDS == null) {
			PROTECTURE_ACQUIRE_CONDS = CoreConditionManager.getInstance().getCoreConditions(1,
					PROTECTURE_VALUE_ADD_COND.getValue());
		}
		return PROTECTURE_ACQUIRE_CONDS;
	}

	public void upgradeCombatSpirit(Player player, CombatSpiritType type, boolean auto) {
		CombatSpirit combatSpirit = player.getCombatSpiritStorage().getCombatSpiritCollection().get(type.getValue());
		CombatSpiritResource resource = manager.getCombatSpiritResource(combatSpirit.getCombatResourceId(), true);
		if (!ModuleOpenManager.getInstance().isOpenByKey(player,
				CombatSpiritManager.getInstance().COMBAT_SPIRIT_OPID.getValue().get(type.name()))) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		if (resource.getNextId() == null || resource.getNextId().length() == 0) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MILITARY_EQUIPMENT_MAX_LEVEL);
			return;
		}

		CoreActions actions = new CoreActions();
		int original = combatSpirit.getGrowUpValue();
		while (resource.getNextId() != null && !resource.getNextId().isEmpty()) {
			CoreConditions conds = resource.getConditions();
			if (combatSpirit.getType() == CombatSpiritType.TREASURE.getValue()) {
				CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.QI,
						resource.getUpgradeNeed());
				actions.addAction(action);
				if (conds.verify(player) && actions.verify(player)
						&& (resource.getNextId() != null && !resource.getNextId().isEmpty())) {
					resource = manager.getCombatSpiritResource(resource.getNextId(), true);
				} else {
					break;
				}
			} else {
				if (conds.verify(player) && original >= resource.getUpgradeNeed()
						&& (resource.getNextId() != null && !resource.getNextId().isEmpty())) {
					original -= resource.getUpgradeNeed();
					resource = manager.getCombatSpiritResource(resource.getNextId(), true);
				} else {
					break;
				}
			}

			if (!auto) {
				break;
			}
		}

		if (resource.getId().equals(combatSpirit.getCombatResourceId())
				&& !resource.getConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		if (resource.getId().equals(combatSpirit.getCombatResourceId())) { // unchange
																			// when
																			// auto
			PacketSendUtility.sendErrorMessage(player, type.getErrCode());
			return;
		}

		if (combatSpirit.getType() == CombatSpiritType.TREASURE.getValue()) {
			actions.act(player, ModuleInfo.valueOf(ModuleType.COMBAT_SPIRIT, SubModuleType.TREASURE_UPGRADE));
		} else {
			combatSpirit.setGrowUpValue(original);
		}

		combatSpirit.setCombatResourceId(resource.getId());
		CombatSpiritResource newResource = manager.getCombatSpiritResource(combatSpirit.getCombatResourceId(), true);
		player.getGameStats().replaceModifiers(StatEffectId.valueOf(type.name(), StatEffectType.COMBAT_SPIRIT),
				CombatSpiritManager.getInstance().getCombatSpiritStats(newResource.getId()), true);
		PacketSendUtility.sendPacket(player, SM_Upgrade_CombatSpirit.valueOf(combatSpirit));
		EventBusManager.getInstance().submit(
				CombatSpiritUpEvent.valueOf(player.getObjectId(), combatSpirit.getCombatResourceId(), type.getValue()));
	}

	public void queryCombatSpirit(Player player) {
		PacketSendUtility.sendPacket(player, SM_Query_CombatSpirit.valueOf(player.getCombatSpiritStorage()));
	}

	public void openCombatSpirit(ModuleOpenEvent event, CombatSpiritType spiritType) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		CombatSpirit s = player.getCombatSpiritStorage().getCombatSpiritCollection().get(spiritType.getValue());
		Map<String, String> initId = CombatSpiritManager.getInstance().COMBAT_SPIRIT_ID_INIT.getValue();
		String resourceId = initId.get(spiritType.name());
		s.setCombatResourceId(resourceId);
		player.getGameStats().replaceModifiers(StatEffectId.valueOf(spiritType.name(), StatEffectType.COMBAT_SPIRIT),
				CombatSpiritManager.getInstance().getCombatSpiritStats(resourceId), true);
		EventBusManager.getInstance().submit(
				CombatSpiritUpEvent.valueOf(player.getObjectId(), resourceId, spiritType.getValue()));

		if (spiritType == CombatSpiritType.PROTECTRUNE) {
			PacketSendUtility.sendPacket(player, SM_CombatSpirit_Reward.valueOf(s, 0));
		}
	}

	public void upgradeMedal(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (!ModuleOpenManager.getInstance().isOpenByKey(player,
				CombatSpiritManager.getInstance().COMBAT_SPIRIT_OPID.getValue().get(CombatSpiritType.MEDAL.name()))) {
			return;
		}
		int inc = 0;
		ObjectResource res = ObjectManager.getInstance().getObjectResource(event.getKey());
		int dif = player.getLevel() - res.getLevel();
		if (dif >= CombatSpiritManager.getInstance().MEDAL_ADD_LEVEL_LIMIT.getValue()) {
			return;
		}
		if (res.getObjectType() == ObjectType.MONSTER) {
			inc = CombatSpiritManager.getInstance().KILL_MONSTER_ADD.getValue();
		} else if (res.getObjectType() == ObjectType.BOSS) {
			inc = CombatSpiritManager.getInstance().KILL_BOSS_ADD.getValue();
		}
		CombatSpirit combatSpirit = player.getCombatSpiritStorage().getCombatSpiritCollection()
				.get(CombatSpiritType.MEDAL.getValue());
		CombatSpiritResource resource = manager.getCombatSpiritResource(combatSpirit.getCombatResourceId(), true);
		if (!resource.getConditions().verify(player)) {
			return;
		}

		Reward reward = Reward.valueOf().addCombatSpiritGrow(CombatSpiritType.MEDAL, inc);
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.COMBAT_SPIRIT, SubModuleType.UPGRADE_MEDAL));
	}

	public void killGainBenifit(Player killer, Player killed) {
		CoreConditions conditions = getProctureAcquireConds();
		Map<String, Player> context = New.hashMap();
		context.put(TriggerContextKey.PLAYER, killer);
		context.put(TriggerContextKey.OTHER_PLAYER, killed);

		if (!conditions.verify(context)) {
			return;
		}
		long now = System.currentTimeMillis();
		Long dayKey = DayKey.valueOf().getLunchTime();

		Integer dailyLimitCount = killer.getCombatSpiritStorage().getDailyLimit().get(dayKey);
		if (dailyLimitCount != null && dailyLimitCount + 1 > PROTECTURE_DAILY_LIMIT.getValue()) {
			PacketSendUtility.sendErrorMessage(killer, ManagedErrorCode.PROTECUTRE_DAILY_LIMIT);
			return;
		}
		dailyLimitCount = dailyLimitCount == null ? 1 : dailyLimitCount + 1;

		// 计算X分钟内，杀同一个人的荣誉收益递减
		Long lastKillTime = killer.getCombatSpiritStorage().getKillLastTime().get(killed.getObjectId());
		Integer killCount = killer.getCombatSpiritStorage().getKillCount().get(killed.getObjectId());
		Integer update = 1;

		if (lastKillTime != null && killCount != null && (now - lastKillTime) < (PROTECTURE_ADD_CD.getValue())) {
			update += killCount;
			if (update > SHORT_TIME_NO_ADD.getValue()) {
				PacketSendUtility.sendErrorMessage(killer, ManagedErrorCode.KILLED_SAME_PLAYER_IN_SHORTTIME);
				return;
			}
		}

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(context,
				ITEM_ACQUIRE_CHOOSERGROUP.getValue());
		Reward itemReward = RewardManager.getInstance().creatReward(killer, rewardIds, null);
		List<RewardItem> rewardItems = itemReward.getItemsByType(RewardType.ITEM);
		killer.getCombatSpiritStorage().logBenifitItems(rewardItems);
		RewardManager.getInstance().grantReward(killer, itemReward,
				ModuleInfo.valueOf(ModuleType.KILL_PEOPLE, SubModuleType.KILL_ENEMY));

		if (!ModuleOpenManager.getInstance().isOpenByKey(
				killer,
				CombatSpiritManager.getInstance().COMBAT_SPIRIT_OPID.getValue()
						.get(CombatSpiritType.PROTECTRUNE.name()))) {
			return;
		}

		killer.getCombatSpiritStorage().getKillCount().put(killed.getObjectId(), update);
		killer.getCombatSpiritStorage().getKillLastTime().put(killed.getObjectId(), now);

		int amount = PROTECTRUNE_INCREMENT.getValue();
		Reward reward = Reward.valueOf().addCombatSpiritGrow(CombatSpiritType.PROTECTRUNE, amount);
		for (RewardItem rewardItem : reward.getItems()) {
			rewardItem.putParms(CombatSpiritGrowProvider.SPECIAL, CombatSpiritGrowProvider.SPECIAL);
		}
		RewardManager.getInstance().grantReward(killer, reward,
				ModuleInfo.valueOf(ModuleType.COMBAT_SPIRIT, SubModuleType.KILL_ENEMY));
		killer.getCombatSpiritStorage().getDailyLimit().put(dayKey, dailyLimitCount);
		killer.getCombatSpiritStorage().addKilledHistory(1);
	}

}
