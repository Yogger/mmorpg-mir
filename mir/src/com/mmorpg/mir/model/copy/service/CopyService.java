package com.mmorpg.mir.model.copy.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.copy.event.LadderResetEvent;
import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.model.CopyDestoryController;
import com.mmorpg.mir.model.copy.model.CopyHistory;
import com.mmorpg.mir.model.copy.model.CopyInfo;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.copy.packet.SM_Copy_Batch;
import com.mmorpg.mir.model.copy.packet.SM_Copy_BossReward;
import com.mmorpg.mir.model.copy.packet.SM_Copy_Encourage;
import com.mmorpg.mir.model.copy.packet.SM_Copy_LadderReward;
import com.mmorpg.mir.model.copy.packet.SM_Get_Copy_Wipe_Reward;
import com.mmorpg.mir.model.copy.packet.SM_HorseEquip_Reset;
import com.mmorpg.mir.model.copy.packet.SM_MingJiangBoss_Reset;
import com.mmorpg.mir.model.copy.packet.SM_Reward_Complete;
import com.mmorpg.mir.model.copy.packet.SM_WarbookCopy_Reset;
import com.mmorpg.mir.model.copy.resource.CopyIndividualBossResource;
import com.mmorpg.mir.model.copy.resource.CopyLadderRewardResource;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.core.consumable.CoreActionType;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.core.consumable.CurrencyAction;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.creater.AbstractObjectCreater;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.CopyEvent;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.service.MapInstanceService;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.RandomUtils;

@Component
public class CopyService {

	public static final long EXP_COPY_DELAY_TIME = 5 * DateUtils.MILLIS_PER_SECOND;

	@Autowired
	private CopyManager copyManager;

	@Autowired
	private CoreConditionManager conditionManager;

	@Autowired
	private TriggerManager triggerManager;

	@Autowired
	private ChooserManager chooserManager;

	@Autowired
	private RewardManager rewardManager;

	/** 铜钱最大鼓舞次数 */
	@Static("COPY:MAX_ENCOURAGE_COPPER")
	private ConfigValue<Integer> MAX_ENCOURAGE_COPPER;
	@Static("COPY:MAX_ENCOURAGE_COPPER_RATE")
	private ConfigValue<Integer> MAX_ENCOURAGE_COPPER_RATE;
	@Static("COPY:MAX_ENCOURAGE_DAMAGE_RATE")
	private ConfigValue<Integer> MAX_ENCOURAGE_DAMAGE_RATE;
	@Static("COPY:LADDER_RESET_CONDITIONIDS")
	private ConfigValue<String[]> LADDER_RESET_CONDITIONIDS;
	/** 名将试炼扫荡的条件 */
	@Static("COPY:LADDER_BATCH_CONDITIONIDS")
	private ConfigValue<String[]> LADDER_BATCH_CONDITIONIDS;

	/** 名将试炼重置的层数 */
	@Static("COPY:LADDER_RESET_COUNT")
	private ConfigValue<Integer> LADDER_RESET_COUNT;

	@Static("tried_action")
	private Formula tried_action;

	public Reward mutiLadderReward(Player player, String copyId) {
		CopyResource copyResource = CopyManager.getInstance().copyResources.get(copyId, true);
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				copyResource.getRewardGroupId());
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds);
		int resetCount = player.getCopyHistory().getLadderCurrentResetCount(copyResource.getIndex());
		Integer[] mutiRates = CopyManager.getInstance().LADDER_RESET_REWARD_RATE.getValue();
		int mutiRateIndex = resetCount;
		if (mutiRateIndex >= mutiRates.length) {
			mutiRateIndex = mutiRates.length - 1;
		}
		reward.mutipleRewards(mutiRates[mutiRateIndex]);
		return reward;
	}

	public void ladderReset(Player player, int sign) {
		player.getCopyHistory().refresh();
		String[] conditionIds = LADDER_RESET_CONDITIONIDS.getValue();
		CoreConditions conditions = conditionManager.getCoreConditions(1, conditionIds);
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		int costGold = (Integer) FormulaParmsUtil.valueOf(tried_action)
				.addParm("n", player.getCopyHistory().getLadderResetCount() + 1).getValue();
		CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, costGold);
		CoreActions actions = new CoreActions();
		actions.addActions(action);
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.LADDER_RESET));
		player.getCopyHistory().addLadderResetCount();
		player.getCopyHistory().resetLadderCopy(player, LADDER_RESET_COUNT.getValue());
		EventBusManager.getInstance().submit(LadderResetEvent.valueOf(player.getObjectId()));
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public void ladderReward(Player player, String id) {
		if (player.getCopyHistory().getLadderRewarded().contains(id)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COPY_LADDER_REWARDED);
			return;
		}

		CopyLadderRewardResource resource = copyManager.getCopyeLadderRewardResources().get(id, true);
		if (!resource.getConditions().verify(player, true)) {
			return;
		}

		rewardManager.grantReward(player, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.LADDER_REWARD));
		player.getCopyHistory().getLadderRewarded().add(id);
		PacketSendUtility.sendPacket(player, SM_Copy_LadderReward.valueOf(id));
	}

	public void batchLadder(Player player) {
		List<String> ladderIds = player.getCopyHistory().getLadderBatchIds();
		if (ladderIds.isEmpty()) {
			return;
		}
		if (!player.getVip().getResource().isCanBatchLadderCopy()) {
			throw new ManagedException(ManagedErrorCode.VIP_LEVEL_CANNOT_BATCHLADDER);
		}
		String[] conditionIds = LADDER_BATCH_CONDITIONIDS.getValue();
		CoreConditions conditions = conditionManager.getCoreConditions(1, conditionIds);
		if (!conditions.verify(player)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		CoreActions actions = new CoreActions();
		Reward reward = Reward.valueOf();
		for (String id : ladderIds) {
			CopyResource resource = copyManager.getCopyResources().get(id, true);
			actions.addActions(resource.getBatchActions());
			reward.addReward(mutiLadderReward(player, id));
		}
		if (!actions.verify(player, true)) {
			return;
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.LADDER_BATCH_ACT));
		for (String id : ladderIds) {
			player.getCopyHistory().addTodayCompleteCount(player, id, 1, false, Integer.MAX_VALUE, true);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.LADDER_BATCH_REWARD));
		player.getCopyHistory().removeLadderCurrenctResetCount(ladderIds);
		PacketSendUtility.sendPacket(player, SM_Copy_Batch.valueOf((ArrayList<String>) ladderIds));
	}

	public void enterCopy(final String id, final Player player, boolean trigger) {
		// 当玩家从爬塔副本走向爬塔副本时特殊判断
		boolean ladderCopy = false;
		String currentCopy = player.getCopyHistory().getCurrentCopyResourceId();
		if (currentCopy != null) {
			CopyResource fromCopy = CopyManager.getInstance().getCopyResources().get(currentCopy, false);
			CopyResource targetCopy = CopyManager.getInstance().getCopyResources().get(id, false);
			if (fromCopy.getType() == CopyType.LADDER && targetCopy.getType() == CopyType.LADDER) {
				ladderCopy = true;
				player.getLifeStats().fullStoreHpAndMp();
			}
		}
		// 在副本时不允许玩家再次进入任何副本
		if (player.isInCopy() && !ladderCopy) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		player.getCopyHistory().refresh();
		final CopyResource resource = copyManager.getCopyResources().get(id, true);
		if (!trigger && !resource.getEnterConditions().verify(player, true)) {
			return;
		}

		if (!resource.vipExtraEnterCondVerify(player)) {
			throw new ManagedException(ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
		}

		if (!resource.getCopyActions().verify(player, true)) {
			return;
		}

		resource.getCopyActions().act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COPY_ENTER_ACT));

		CoreActions specialActions = resource.getEnterSpecialCoreActions(player);
		if (specialActions != null) {
			if (!specialActions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			specialActions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COPY_ENTER_ACT));
		}

		// 构建副本
		final WorldMapInstance worldMapInstance = MapInstanceService.createCommonMapCopy(resource.getMapId(), false);
		player.getCopyHistory().enterCopy(worldMapInstance, player, resource);
		CopyInfo copyInfo = worldMapInstance.getCopyInfo();
		copyInfo.setCopyId(id);
		copyInfo.setMapId(resource.getMapId());
		copyInfo.setCreateTime(worldMapInstance.getCreateTime());
		copyInfo.setFailNotReturnSpecialAct(false);

		if (resource.getType() == CopyType.EXP) {
			copyInfo.setCopyDestoryController(new CopyDestoryController() {
				@Override
				public boolean destory() {
					long destoryTime = worldMapInstance.getCreateTime()
							+ resource.getCopyDestoryTime() * DateUtils.MILLIS_PER_SECOND;
					return System.currentTimeMillis() > destoryTime;
				}
			});
		}

		// 开启触发器
		Map<String, Object> contexts = New.hashMap();
		contexts.put(TriggerContextKey.MAP_INSTANCE, worldMapInstance);
		contexts.put(TriggerContextKey.PLAYER, player);
		for (String triggerId : resource.getTriggers()) {
			triggerManager.trigger(contexts, triggerId);
		}
		MapInstanceService.startInstanceChecker(worldMapInstance);
		EventBusManager.getInstance().submit(CopyEvent.valueOf(player, id, resource.getType()));
		// VIP副本的怪物需要根据玩家等级重新设置属性
		if (resource.getType() == CopyType.VIP) {
			Iterator<VisibleObject> vos = worldMapInstance.objectIterator();
			while (vos.hasNext()) {
				VisibleObject vo = vos.next();
				if (vo.getObjectType() == ObjectType.MONSTER) {
					Monster monster = (Monster) vo;
					AbstractObjectCreater.getCreater(ObjectType.MONSTER).setStats(monster, monster.getObjectResource(),
							player.getLevel());
					monster.getLifeStats().synchronizeWithMaxStats();
				}
			}
		} else if (resource.getType() == CopyType.MINGJIANG) {
			List<VisibleObject> visObjs = worldMapInstance.findObjectByType(ObjectType.BOSS);
			for (VisibleObject visObject : visObjs) {
				if (visObject instanceof Boss) {
					BossController bossController = (BossController) visObject.getController();
					bossController.setBroad(false);
					((Boss) visObject).getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
						@Override
						public void die(Creature creature) {
							player.getCopyHistory().getCurrentMapInstance().getCopyInfo()
									.setFailNotReturnSpecialAct(true);
						}
					});
				}
			}
		}

		if (CopyManager.copyControllers.containsKey(id)) {
			CopyManager.copyControllers.get(id).enterCopy(player, worldMapInstance, resource);
		}
		player.getEffectController().removeEffect(ConfigValueManager.getInstance().AURA_APPLYTO_STATSKILLID.getValue());

		LogManager.copy(player.getPlayerEnt().getAccountName(), player.getObjectId(), player.getName(),
				System.currentTimeMillis(), id, 1);
	}

	public void leaveCopy(Player player) {
		String id = player.getCopyHistory().getCurrentCopyResourceId();
		if (id == null) {
			return;
		}
		player.getMoveController().stopMoving(); // 停下来
		if (player.getCopyHistory().getCopyController() != null) {
			player.getCopyHistory().getCopyController().leaveCopyBefore(player);
		}
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		// if (resource.getRewardGroupId() != null
		// &&
		// !player.getCopyHistory().getCurrentMapInstance().getCopyInfo().isReward())
		// {
		// List<String> rewardIds = chooserManager.chooseValueByRequire(player,
		// resource.getRewardGroupId());
		// rewardManager.grantReward(player, rewardIds,
		// ModuleInfo.valueOf(resource.getRewardLogType(),
		// SubModuleType.COPY_REWARD));
		// }
		if (resource.getType() == CopyType.WARBOOK) {
			boolean canReward = true;
			player.getCopyHistory().refresh();
			if (player.getCopyHistory().getCurrentMapInstance() == null) {
				player.sendUpdatePosition();
				canReward = false;
			}
			CopyInfo copyInfo = player.getCopyHistory().getCurrentMapInstance().getCopyInfo();
			if (player.getCopyHistory().getCurrentMapInstance() == null) {
				player.sendUpdatePosition();
				canReward = false;
			}
			if (copyInfo.isReward()) {
				canReward = false;
			}
			if (!copyInfo.getCopyId().equals(id)) {
				canReward = false;
			}
			if (!copyInfo.isCopyComplet()) {
				canReward = false;
			}
			if (!resource.getRewardConditions().verify(player, false)) {
				canReward = false;
			}
			if (canReward) {
				List<String> rewardIds = chooserManager.chooseValueByRequire(player, resource.getRewardGroupId());
				Reward reward = rewardManager.grantReward(player, rewardIds,
						ModuleInfo.valueOf(resource.getRewardLogType(), SubModuleType.COPY_REWARD));
				PacketSendUtility.sendPacket(player, SM_Reward_Complete.valueOf(reward));
				player.getCopyHistory().getCurrentMapInstance().getCopyInfo().setReward(true);
			}
		}

		if (resource.getType() == CopyType.HORSEEQUIP) {
			boolean canReward = true;
			player.getCopyHistory().refresh();
			if (player.getCopyHistory().getCurrentMapInstance() == null) {
				player.sendUpdatePosition();
				canReward = false;
			}
			CopyInfo copyInfo = player.getCopyHistory().getCurrentMapInstance().getCopyInfo();
			if (player.getCopyHistory().getCurrentMapInstance() == null) {
				player.sendUpdatePosition();
				canReward = false;
			}
			if (copyInfo.isReward()) {
				canReward = false;
			}
			if (!copyInfo.getCopyId().equals(id)) {
				canReward = false;
			}
			if (!resource.getRewardConditions().verify(player, false)) {
				canReward = false;
			}

			Integer curHorseEquipQuest = player.getCopyHistory().getCurHorseEquipQuest();
			if (curHorseEquipQuest == null) {
				canReward = false;
			}
			if (canReward) {
				String rewardId = copyManager.HORSEEQUIP_REWARD.getValue().get(curHorseEquipQuest + "");
				rewardManager.grantReward(player, rewardId,
						ModuleInfo.valueOf(resource.getRewardLogType(), SubModuleType.COPY_REWARD));
				player.getCopyHistory().getCurrentMapInstance().getCopyInfo().setReward(true);
			}
		}

		if (resource.getType() == CopyType.MINGJIANG
				&& !player.getCopyHistory().getCurrentMapInstance().getCopyInfo().isFailNotReturnSpecialAct()) {
			String rewardId = resource.getEnterSpecialReturnReward(player);
			if (rewardId != null) {
				rewardManager.grantReward(player, rewardId,
						ModuleInfo.valueOf(ModuleType.COPY_RETURN_ACTIONS, SubModuleType.MINGJIANG_ACTIONS_RETURN));
			}
		}

		player.getCopyHistory().leaveCopy();
		if (player.getLifeStats().isAlreadyDead() || (id != null && resource.getType() != CopyType.LADDER)) {
			player.getLifeStats().fullStoreHpAndMp();
		}

		if (player.getCopyHistory().getCopyController() != null) {
			player.getCopyHistory().getCopyController().leaveCopy(player);
			player.getCopyHistory().setCopyController(null);
		}

		// 检查移除鼓舞BUFF
		if (player.getEffectController().contains("INSPIRE_ATTACK")) {
			player.getEffectController().getAnormalEffect("INSPIRE_ATTACK").endEffect();
		}
		if (player.getEffectController().contains("INSPIRE_HP")) {
			player.getEffectController().getAnormalEffect("INSPIRE_HP").endEffect();
		}
		LogManager.copy(player.getPlayerEnt().getAccountName(), player.getObjectId(), player.getName(),
				System.currentTimeMillis(), id, -1);

	}

	public void reward(String id, Player player) {
		player.getCopyHistory().refresh();
		if (player.getCopyHistory().getCurrentMapInstance() == null) {
			player.sendUpdatePosition();
			return;
		}
		if (player.getCopyHistory().getCurrentMapInstance().getCopyInfo().isReward()) {
			return;
		}
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		if (!resource.getRewardConditions().verify(player, true)) {
			return;
		}
		if (resource.getType() == CopyType.MINGJIANG) {
			List<String> rewardIds = chooserManager.chooseValueByRequire(player, resource.getRewardGroupId());
			Reward reward = rewardManager.creatReward(player, rewardIds);
			PacketSendUtility.sendPacket(player, SM_Reward_Complete.valueOf(reward));
		} else if (resource.getType() == CopyType.HORSEEQUIP) {
			Integer curHorseEquipQuest = player.getCopyHistory().getCurHorseEquipQuest();
			if (curHorseEquipQuest == null) {
				return;
			}
			QuestResource questResource = QuestManager.getInstance().questResources
					.get(String.valueOf(curHorseEquipQuest), true);
			if (curHorseEquipQuest == null || !resource.getId().equals(questResource.getCopyId())) {
				return;
			}
			String rewardId = copyManager.HORSEEQUIP_REWARD.getValue().get(curHorseEquipQuest + "");
			Reward reward = rewardManager.creatReward(player, rewardId, null);
			PacketSendUtility.sendPacket(player, SM_Reward_Complete.valueOf(reward));
		} else {
			List<String> rewardIds = chooserManager.chooseValueByRequire(player, resource.getRewardGroupId());
			Reward reward = rewardManager.grantReward(player, rewardIds,
					ModuleInfo.valueOf(resource.getRewardLogType(), SubModuleType.COPY_REWARD));
			PacketSendUtility.sendPacket(player, SM_Reward_Complete.valueOf(reward));
			player.getCopyHistory().getCurrentMapInstance().getCopyInfo().setReward(true);
		}
	}

	public void showReward(Player player, String id) {
		player.getCopyHistory().refresh();
		if (player.getCopyHistory().getCurrentMapInstance() == null) {
			player.sendUpdatePosition();
			return;
		}

		CopyResource resource = copyManager.getCopyResources().get(id, true);

		if (resource.getType() == CopyType.HORSEEQUIP) {
			Integer curHorseEquipQuest = player.getCopyHistory().getCurHorseEquipQuest();
			if (curHorseEquipQuest == null) {
				return;
			}
			QuestResource questResource = QuestManager.getInstance().questResources
					.get(String.valueOf(curHorseEquipQuest), true);
			if (curHorseEquipQuest == null || !resource.getId().equals(questResource.getCopyId())) {
				return;
			}
			String rewardId = copyManager.HORSEEQUIP_REWARD.getValue().get(curHorseEquipQuest + "");
			Reward reward = rewardManager.creatReward(player, rewardId, null);
			PacketSendUtility.sendPacket(player, SM_Reward_Complete.valueOf(reward));
		}

	}

	@Override
	public void clearExpCopyMonster(Player player) {
		if (!player.isInCopy()) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		CopyResource resource = copyManager.getCopyResources().get(player.getCopyHistory().getCurrentCopyResourceId(),
				false);
		if (resource == null) {
			return;
		}
		if (resource.getType() != CopyType.EXP) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (!resource.getEncourageConditions().verify(player, true)) {
			return;
		}

		WorldMapInstance worldMapInstance = player.getCopyHistory().getCurrentMapInstance();
		if (worldMapInstance != null) {
			Iterator<VisibleObject> iterator = worldMapInstance.objectIterator();
			while (iterator.hasNext()) {
				VisibleObject vo = iterator.next();
				if (vo instanceof Summon) {
					continue;
				}
				vo.getController().delete();
			}
		}
	}

	public void clearHorseEquipMonster(Player player) {
		if (!player.isInCopy()) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		CopyResource resource = copyManager.getCopyResources().get(player.getCopyHistory().getCurrentCopyResourceId(),
				false);
		if (resource == null) {
			return;
		}
		if (resource.getType() != CopyType.HORSEEQUIP) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		WorldMapInstance worldMapInstance = player.getCopyHistory().getCurrentMapInstance();
		if (worldMapInstance != null) {
			Iterator<VisibleObject> iterator = worldMapInstance.objectIterator();
			while (iterator.hasNext()) {
				VisibleObject vo = iterator.next();
				if (vo instanceof Summon) {
					continue;
				} else if (vo instanceof Player) {
					continue;
				} else if (vo.getObjectType() == ObjectType.DROPOBJECT) {
					continue;
				}

				vo.getController().delete();
			}
		}
	}

	public void encourge(Player player, boolean gold) {
		if (!player.isInCopy()) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		CopyResource resource = copyManager.getCopyResources().get(player.getCopyHistory().getCurrentCopyResourceId(),
				true);
		if (resource.getType() != CopyType.EXP) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (!resource.getEncourageConditions().verify(player, true)) {
			return;
		}

		CopyInfo copyInfo = player.getCopyHistory().getCurrentMapInstance().getCopyInfo();

		if (copyInfo.getHpCount() >= resource.getHpEncourageSkills().length
				&& copyInfo.getDamageCount() >= resource.getDamageEncourageSkills().length) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COPY_ENCOURAGE_COUNT_MAX);
			return;
		}

		CoreActions actions = new CoreActions();
		boolean success = true;
		if (gold) {
			actions.addActions(CoreActionManager.getInstance().getCoreActions(1, resource.getEncourageGoldActs()));
		} else {
			// 两种buff均>=5层时，点击“铜钱鼓舞”按钮于鼠标旁返回提示：“鼓舞等级过高，无法使用铜钱鼓舞”
			if (copyInfo.getHpCount() >= MAX_ENCOURAGE_COPPER.getValue()
					&& copyInfo.getDamageCount() >= MAX_ENCOURAGE_COPPER.getValue()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COPY_COPPER_ENCOURGE_COUNT);
				return;
			}
			actions.addActions(CoreActionManager.getInstance().getCoreActions(1, resource.getEncourageCopperActs()));
			if (!RandomUtils.isHit(MAX_ENCOURAGE_COPPER_RATE.getValue() * 1.0 / 10000)) {
				success = false;
			}
		}
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COPY_ENCOURAGE_ACT));
		if (success) {
			int newCount = copyInfo.addEncourgeCount(1);
			int skillId = 0;
			if (gold) {
				boolean encourageDamageRate = RandomUtils.isHit(MAX_ENCOURAGE_DAMAGE_RATE.getValue() * 1.0 / 10000);
				if ((copyInfo.getHpCount() >= resource.getHpEncourageSkills().length || encourageDamageRate)
						&& copyInfo.getDamageCount() < resource.getDamageEncourageSkills().length) {
					// 攻击
					int count = copyInfo.addDamageEncourgeCount(1);
					skillId = resource.getDamageEncourageSkills()[count - 1];
				} else {
					int count = copyInfo.addHpEncourgeCount(1);
					skillId = resource.getHpEncourageSkills()[count - 1];
				}
			} else {
				boolean encourageDamageRate = RandomUtils.isHit(MAX_ENCOURAGE_DAMAGE_RATE.getValue() * 1.0 / 10000);
				// 铜钱鼓舞最多可将buff叠加5层（若一种buff>=5层，鼓舞成功时必定添加另一种buff）
				boolean copperEncourageCountMax = copyInfo.getDamageCount() < MAX_ENCOURAGE_COPPER.getValue();
				if (copperEncourageCountMax
						&& (copyInfo.getHpCount() >= MAX_ENCOURAGE_COPPER.getValue() || encourageDamageRate)) {
					// 攻击
					int count = copyInfo.addDamageEncourgeCount(1);
					skillId = resource.getDamageEncourageSkills()[count - 1];
				} else {
					int count = copyInfo.addHpEncourgeCount(1);
					skillId = resource.getHpEncourageSkills()[count - 1];
				}
			}

			PacketSendUtility.sendPacket(player,
					SM_Copy_Encourage.valueOf(newCount, copyInfo.getHpCount(), copyInfo.getDamageCount(), true));
			copyInfo.setSkillId(skillId);
			Skill skill = SkillEngine.getInstance().getSkill(null, skillId, player.getObjectId(), 0, 0, player, null);
			long duration = resource.getCopyDestoryTime() * DateUtils.MILLIS_PER_SECOND
					- (System.currentTimeMillis() - player.getCopyHistory().getCurrentMapInstance().getCreateTime())
					+ EXP_COPY_DELAY_TIME;
			for (final Effect effect : skill.noEffectorUseSkill(duration)) {
				ActionObserver leaveCopyOb = new ActionObserver(ObserverType.LEAVE_COPY) {
					@Override
					public void leaveCopy() {
						effect.endEffect();
					}
				};
				player.getObserveController().attach(leaveCopyOb);
			}
		} else {
			PacketSendUtility.sendPacket(player, SM_Copy_Encourage.valueOf(copyInfo.getEncourgeCount(),
					copyInfo.getHpCount(), copyInfo.getDamageCount(), false));
		}
	}

	public void buyCount(Player player, String id) {
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		CopyHistory history = player.getCopyHistory();
		if (!resource.getBuyCountConditions().verify(player, true)) {
			return;
		}
		if (resource.getType() == CopyType.WARBOOK) {
			int exCount = resource.getVipDailyBuyCount().get(player.getVip().getLevel() + "");
			if (history.getBuyCount(id) >= exCount) {
				throw new ManagedException(ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
			}
			int enterCount = history.getEnterHistory().get(id) == null ? 0 : history.getTodayEnterHistory().get(id);
			if (enterCount == 0 || enterCount - history.getBuyCount(id) != 1) {
				throw new ManagedException(ManagedErrorCode.SYS_ERROR);
			}
		}
		CoreActions actions = new CoreActions();
		actions.addActions(CoreActionManager.getInstance().getCoreActions(1,
				resource.getBuyCountAct()[player.getCopyHistory().getBuyCount(id)]));
		actions.verify(player, true);
		if (resource.getType() == CopyType.WARBOOK) {
			actions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.WARBOOK_COPY_RESET, id));
		} else {
			actions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COPY_BUY_COUNT));
		}
		player.getCopyHistory().addBuyCount(id, 1);
	}

	public void bossCopyReward(Player player, String id) {
		CopyIndividualBossResource resource = copyManager.getCopyIndividualBossResource(id);
		if (!resource.getConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ERROR_MSG);
			return;
		}

		rewardManager.grantReward(player, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.PRIVATE_BOSS_COPY));
		player.getCopyHistory().getBossRewarded().add(id);

		PacketSendUtility.sendPacket(player, SM_Copy_BossReward.valueOf(id));
	}

	@Override
	public void individualBossReset(Player player, String id) {
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		if (resource.getType() != CopyType.BOSS) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!player.getCopyHistory().getCompleteHistory().containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.COPY_NOT_COMPLETE);
		}
		if (!resource.getEnterConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.COPY_TODAY_ALREADY_BATCH);
		}
		if (!resource.getCopyActions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		resource.getCopyActions().act(player,
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.BOSS_COPY_BATCH_ACTIONS));
		List<String> copyRewardIds = chooserManager.chooseValueByRequire(player, resource.getRewardGroupId());
		Reward drop = rewardManager.grantReward(player, resource.getWipeOutReward(player),
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.BOSS_COPY_BATCH_REWARD));
		Reward finished = rewardManager.creatReward(player, copyRewardIds);
		finished.addReward(resource.getBossCoinsReward(player));
		rewardManager.grantReward(player, finished,
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.BOSS_COPY_BATCH_REWARD));
		player.getCopyHistory().addTodayEnterCount(id, 1, false);
		player.getCopyHistory().addTodayCompleteCount(player, id, 1, false, 0, true);

		PacketSendUtility.sendPacket(player, SM_Get_Copy_Wipe_Reward.valueOf(id, finished, drop));
	}

	@Override
	public void mingJiangBossReset(Player player, String id) {
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		// 判断条件
		if (resource.getType() != CopyType.MINGJIANG) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!resource.getEnterConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.COPY_ENTER_NO_CONDITION);
		}
		if (!resource.vipExtraEnterCondVerify(player)) {
			throw new ManagedException(ManagedErrorCode.COPY_ENTER_NO_CONDITION);
		}
		// 判断完成条件
		if (!player.getCopyHistory().getCompleteHistory().containsKey(id)
				|| player.getCopyHistory().getCompleteHistory().get(id) < 1) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		// 判断消耗
		if (!resource.getEnterSpecialCoreActions(player).verify(player)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		resource.getEnterSpecialCoreActions(player).act(player,
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.MINGJIANG_BOSS_COPY_ACTIONS));
		// 发放奖励
		Reward drop = rewardManager.grantReward(player, resource.getMingJiangReward(player),
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.MINGJIANG_BOSS_COPY_REWARD));
		// 记录日志
		player.getCopyHistory().addTodayEnterCount(id, 1, false);
		player.getCopyHistory().addTodayCompleteCount(player, id, 1, false, 0, true);
		// 返回前端
		PacketSendUtility.sendPacket(player, SM_MingJiangBoss_Reset.valueOf(id, drop));
	}

	public void horseCopyReset(Player player, String id) {
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		if (resource.getType() != CopyType.HORSEEQUIP) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (!resource.getEnterConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.COPY_ENTER_NO_CONDITION);
		}

		if (!player.getCopyHistory().getCompleteHistory().containsKey(id)
				|| player.getCopyHistory().getCompleteHistory().get(id) < 1) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}

		if (!resource.vipExtraEnterCondVerify(player)) {
			throw new ManagedException(ManagedErrorCode.COPY_ENTER_NO_CONDITION);
		}

		// 判断消耗
		if (!resource.getEnterSpecialCoreActions(player).verify(player)) {
			throw new ManagedException(ManagedErrorCode.COPY_HORSEEQUIP_RESET_ACTION_NOT_VERIFY);
		}

		resource.getEnterSpecialCoreActions(player).act(player,
				ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.HORSEEQUIP_COPY_ACTIONS));

		Integer horseEquipMaxQuestHis = player.getCopyHistory().getHorseEquipMaxQuestHis().get(id);
		if (horseEquipMaxQuestHis == null || horseEquipMaxQuestHis < Integer
				.parseInt(copyManager.HORSEEQUIP_RESET_MIN_QUESTID.getValue().get(id))) {
			throw new ManagedException(ManagedErrorCode.COPY_HORSEEQUIP_RESET_MIN_QUEST);
		}

		String rewardId = CopyManager.getInstance().HORSEEQUIP_REWARD.getValue().get(horseEquipMaxQuestHis + "");
		Reward reward = rewardManager.creatReward(player, rewardId, null);
		List<String> rewardIds = chooserManager.chooseValueByRequire(player,
				copyManager.HORSEEQUIP_BOSS_DROP.getValue().get(id));
		Reward bossDrop = rewardManager.creatReward(player, rewardIds);
		Reward r = Reward.valueOf();
		r.addReward(reward);
		r.addReward(bossDrop);
		RewardManager.getInstance().grantReward(player, r,
				ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_REWARD));
		player.getCopyHistory().addTodayEnterCount(id, 1, false);
		player.getCopyHistory().addTodayCompleteCount(player, id, 1, false, 0, false);
		PacketSendUtility.sendPacket(player, SM_HorseEquip_Reset.valueOf(id, reward, bossDrop));
	}

	/**
	 * 兵书副本扫荡
	 */
	public void warbookReset(Player player, String id, boolean doubled) {
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		if (resource.getType() != CopyType.WARBOOK) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!resource.getEnterConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.COPY_ENTER_NO_CONDITION);
		}

		if (!player.getCopyHistory().getCompleteHistory().containsKey(id)
				|| player.getCopyHistory().getCompleteHistory().get(id) < 1) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}

		// 双倍
		if (doubled == true) {
			CoreActions actions = resource.getSweepActions();
			// 判断消耗
			if (!actions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			actions.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.WARBOOK_COPY_RESET_ACT));
		}

		Reward r = Reward.valueOf();

		List<String> rewardIds = chooserManager.chooseValueByRequire(player, resource.getRewardGroupId());
		Reward reward = rewardManager.creatReward(player, rewardIds);
		if (doubled == true) {
			reward.mutipleRewards(2);
		}
		r.addReward(reward);
		rewardIds = chooserManager.chooseValueByRequire(player, copyManager.WARBOOK_BOSS_DROP.getValue().get(id));
		Reward bossDrop = rewardManager.creatReward(player, rewardIds);
		r.addReward(bossDrop);
		rewardManager.grantReward(player, r,
				ModuleInfo.valueOf(resource.getRewardLogType(), SubModuleType.WARBOOK_COPU_RESET_REWARD));
		player.getCopyHistory().addTodayEnterCount(id, 1, false);
		player.getCopyHistory().addTodayCompleteCount(player, id, 1, false, 0, false);
		PacketSendUtility.sendPacket(player, SM_WarbookCopy_Reset.valueOf(id, reward, bossDrop));
	}

	@Override
	public void warBookReward(Player player, String id) {
		player.getCopyHistory().refresh();
		CopyInfo copyInfo = player.getCopyHistory().getCurrentMapInstance().getCopyInfo();
		if (player.getCopyHistory().getCurrentMapInstance() == null) {
			player.sendUpdatePosition();
			return;
		}
		if (copyInfo.isReward()) {
			return;
		}
		if (!copyInfo.getCopyId().equals(id)) {
			return;
		}
		if (!copyInfo.isCopyComplet()) {
			return;
		}
		CopyResource resource = copyManager.getCopyResources().get(id, true);
		if (!resource.getRewardConditions().verify(player, true)) {
			return;
		}
		CoreActions action = resource.getDoubleRewardCoreActions(player);
		if (action != null) {
			action.verify(player, true);
		}
		action.act(player, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.WAR_BOOK_DOUBLE_ACTION));
		List<String> rewardIds = chooserManager.chooseValueByRequire(player, resource.getRewardGroupId());
		rewardIds.addAll(rewardIds);
		Reward reward = rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(resource.getRewardLogType(), SubModuleType.WAR_BOOK_DOUBLE_REWARD));
		PacketSendUtility.sendPacket(player, SM_Reward_Complete.valueOf(reward));
		player.getCopyHistory().getCurrentMapInstance().getCopyInfo().setReward(true);
	}

}
