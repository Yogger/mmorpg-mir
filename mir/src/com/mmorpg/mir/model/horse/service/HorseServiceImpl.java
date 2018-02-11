package com.mmorpg.mir.model.horse.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.horse.event.HorseGradeUpEvent;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.horse.model.HorseAppearance;
import com.mmorpg.mir.model.horse.packet.SM_Cancel_Illution;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;
import com.mmorpg.mir.model.horse.packet.SM_Horse_Notify;
import com.mmorpg.mir.model.horse.packet.SM_Illution_Active;
import com.mmorpg.mir.model.horse.packet.SM_Illution_Use;
import com.mmorpg.mir.model.horse.packet.SM_RideBroadcast;
import com.mmorpg.mir.model.horse.resource.HorseResource;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

@Component
public class HorseServiceImpl implements HorseService {

	@Autowired
	private HorseManager horseManager;
	@Autowired
	private CoreActionManager coreActionManager;
	@Autowired
	private ChooserManager chooserManager;

	public void ride(Player player) {
		player.ride();
	}

	public void unRide(Player player) {
		player.unRide();
	}

	// 老服用户
	public void doOldUpgrade(Player player) {
		Horse horse = player.getHorse();

		List<HorseResource> horseStorage = new ArrayList<HorseResource>(horseManager.horseResourceStorage.getAll());
		Collections.sort(horseStorage, new HorseResource.GradeComparator());

		int grade = horse.getGrade();
		int level = horse.getLevel();
		int upCount = horse.getUpSum();
		for (HorseResource resource : horseStorage) {
			if (resource.getId() < grade) {
				continue;
			}

			// 本阶等级数
			int levelCount = resource.getNeedCount().length;
			int[] needCount = resource.getNeedCount();
			boolean breaked = false;
			for (int i = level; i < levelCount; i++) {
				if (upCount < needCount[i]) {
					breaked = true;
					break;
				}
				upCount -= needCount[i];
				if (i == levelCount - 1) {
					grade = resource.getId() + 1;
					level = 0;
				} else {
					level = i + 1;
				}
			}

			if (breaked) {
				break;
			}
		}
		if (grade != horse.getGrade() || level != horse.getLevel()) {
			horse.setGrade(grade);
			horse.setLevel(level);
			horse.setUpSum(upCount);
			LogManager.horseUpgrade(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(),
					player.getName(), player.getObjectId(), grade, level, horse.getUpSum(), System.currentTimeMillis());
			// 老玩家修改
			flushHorse(player, true, false, false);
		}
	}

	private void broadCastCriActivity(Player player, int addCount, String chatIl18n, String tvIl18n) {
		if (horseManager.BROADCAST_LEAST_RATE.getValue() <= addCount) {
			I18nUtils utils = I18nUtils.valueOf(chatIl18n);
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm("n", I18nPack.valueOf(addCount + ""));
			ChatManager.getInstance().sendSystem(11001, utils, null);

			// tv
			I18nUtils tvUtils = I18nUtils.valueOf(tvIl18n);
			tvUtils.addParm("name", I18nPack.valueOf(player.getName()));
			tvUtils.addParm("n", I18nPack.valueOf(addCount + ""));
			ChatManager.getInstance().sendSystem(6, tvUtils, null, player.getCountry());
		}

	}

	public SM_HorseUpdate upgradeHorse(Player player, boolean autoBuy) {
		Horse horse = player.getHorse();
		HorseResource hr = horseManager.getHorseResource(horse.getGrade());
		if (hr.getCount() == 0 && horse.isMaxHorseLevel()) {
			throw new ManagedException(ManagedErrorCode.HORE_MAX_LEVEL);
		}
		doOldUpgrade(player);

		// 计算消耗
		CoreActions actions = new CoreActions();
		// 先计算优先道具，先消耗 限时道具，不够再消耗剩余所需份数道具，勾选自动购买，不足再消耗剩余所需份数的元宝
		int criItemCount = 0;
		// 周暴击活动
		boolean canWeekCri = false;
		if (hr.getMaterialActionFirstPri() != null && hr.getMaterialActionFirstPri().length != 0) {
			// 暴击丹
			String criItemId = coreActionManager.getCoreActions(1, hr.getMaterialActionFirstPri()).getFirstItemKey();
			criItemCount = (int) player.getPack().getItemSizeByKey(criItemId);
		}

		if (hr.getCount() <= criItemCount) {
			// 优先扣除暴击丹
			canWeekCri = true;
			CoreActions priActions = coreActionManager.getCoreActions(hr.getCount(), hr.getMaterialActionFirstPri());
			actions.addActions(priActions);
		} else {
			int needItemCount = hr.getCount() - criItemCount;
			if (criItemCount > 0) {
				// 扣除暴击丹
				actions.addActions(coreActionManager.getCoreActions(criItemCount, hr.getMaterialActionFirstPri()));
			}
			int priItemCount = 0;
			if (hr.getMaterialActionsPriority() != null && hr.getMaterialActionsPriority().length != 0) {
				//
				String itemId = coreActionManager.getCoreActions(1, hr.getMaterialActionsPriority()).getFirstItemKey();
				priItemCount = (int) player.getPack().getItemSizeByKey(itemId);
			}
			if (needItemCount <= priItemCount) {
				actions.addActions(coreActionManager.getCoreActions(needItemCount, hr.getMaterialActionsPriority()));
			} else {
				if (priItemCount > 0) {
					actions.addActions(coreActionManager.getCoreActions(priItemCount, hr.getMaterialActionsPriority()));
				}
				needItemCount = needItemCount - priItemCount;
				CoreActions itemActions = coreActionManager.getCoreActions(needItemCount, hr.getMaterialActions());
				String trueItemId = itemActions.getFirstItemKey();
				int itemHave = (int) player.getPack().getItemSizeByKey(trueItemId);
				if (needItemCount > itemHave && !autoBuy) {
					throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
				} else {
					int needGold = needItemCount - itemHave;
					if (needGold <= 0) {
						actions.addActions(coreActionManager.getCoreActions(needItemCount, hr.getMaterialActions()));
					} else {
						if (itemHave > 0) {
							actions.addActions(coreActionManager.getCoreActions(itemHave, hr.getMaterialActions()));
						}
						actions.addActions(coreActionManager.getCoreActions(needGold, hr.getGoldActions()));
					}
				}
			}
		}

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.HORSE, SubModuleType.HORSE_UPGRADE);
		actions.verify(player, true);
		actions.act(player, moduleInfo);

		int addCount = 1;
		if (horseManager.getCriActivityConds().verify(player)) {
			String value = chooserManager.chooseValueByRequire(player,
					horseManager.CRI_ACTIVITY_CHOSERGROUPID.getValue()).get(0);
			addCount = Integer.parseInt(value);
		}
		// 跟上面的暴击活动不冲突
		if (canWeekCri) {
			String value = chooserManager.chooseValueByRequire(player, horseManager.WEEK_CRI_CHOOSER_ID.getValue())
					.get(0);
			addCount = Integer.parseInt(value);
		}
		horse.addUpSum(addCount);
		HorseResource resource = horseManager.getHorseResource(horse.getGrade());
		int needCount = resource.getNeedCount()[horse.getLevel()];
		if (horse.getUpSum() >= needCount) {
			// 升级
			if (horse.isMaxHorseLevel()) {
				addGrade(player);
			} else {
				addLevel(player);
			}
			flushHorse(player, true, true, false);
		}
		String chatIl18n = "60103";
		String tvIl18n = "307011";
		if (canWeekCri) {
			chatIl18n = "60106";
			tvIl18n = "307014";
		}
		broadCastCriActivity(player, addCount, chatIl18n, tvIl18n);
		SM_HorseUpdate sm = player.getHorse().createVO();
		sm.setActions(actions);
		sm.setAddCount(addCount);
		return sm;
	}

	public void useBlessItem(Player player, int blessValue) {
		/*
		 * Horse horse = player.getHorse(); HorseResource hr =
		 * horseManager.getHorseResource(horse.getGrade()); if (hr.getCount() ==
		 * 0) { throw new ManagedException(ManagedErrorCode.HORE_MAX_LEVEL); }
		 * horse.addUpSum(blessValue /
		 * horseManager.BLESSVALUE_CONVER_COUNT.getValue()); if
		 * (horse.getUpSum() >= hr.getMaxSum()) { addGrade(player);
		 * flushHorse(player, true, false); } else if (horse.getUpSum() <=
		 * hr.getMinSum()) { addBlessValue(player, blessValue); } else { int
		 * rate = 0; if (horse.isMaxFailCount()) { rate = 10000; } else { rate =
		 * (Integer) FormulaParmsUtil.valueOf(UPGRADE_RATE) .addParm("count",
		 * player.getHorse().getBlessValue()) .addParm("grade",
		 * player.getHorse().getGrade()).getValue(); } if
		 * (RandomUtils.isHit(rate * 1.0 / 10000)) { addGrade(player);
		 * flushHorse(player, true, false); } else { addBlessValue(player,
		 * blessValue); } }
		 */
		// SM_HorseUpdate sm = player.getHorse().createVO();
		// PacketSendUtility.sendPacket(player, sm);
	}

	public void addGrade(Player player) {
		player.getHorse().addGrade();
		player.getHorse().setLevel(0);
		noticeBroadCast(player, player.getHorse());
		player.getHorse().clear();
	}

	public void addLevel(Player player) {
		player.getHorse().addLevel();
		player.getHorse().clear();
	}

	private void noticeBroadCast(Player player, Horse horse) {
		if (horse.getGrade() >= horseManager.START_BROADCAST.getValue()) {
			// chat div
			I18nUtils utils = I18nUtils.valueOf("307001");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("horse", I18nPack.valueOf(horseManager.HORSE_NAME.getValue().get(horse.getGrade() + "")));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(0, utils, null);

			// tv
			I18nUtils tvUtils = I18nUtils.valueOf("60101", utils);
			ChatManager.getInstance().sendSystem(11001, tvUtils, null);
		}
	}

	public void flushHorse(Player player, boolean isUpgrade, boolean clear, boolean sendPacket) {
		// 刷新人物属性
		addStats(player);
		// 升级
		if (isUpgrade) {
			Horse horse = player.getHorse();
			if (clear) {
				horse.setUpSum(0);
				horse.setClearTime(0);
			}
			EventBusManager.getInstance().submit(HorseGradeUpEvent.valueOf(player));
		}

		// 骑乘就广播
		if (player.isRide())
			PacketSendUtility.broadcastPacket(player, SM_RideBroadcast.valueOf(player), true);
		// 推送坐骑数据
		if (sendPacket)
			PacketSendUtility.sendPacket(player, SM_Horse_Notify.valueOf(player));
	}

	public void addStats(Player player) {
		HorseResource res = horseManager.getHorseResource(player.getHorse().getGrade());
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.HORSE)) {
			player.getGameStats().replaceModifiers(Horse.GAME_STATE_ID, player.getHorse().getStat(), true);
			Stat[] levelStats = PlayerManager.getInstance().getPlayerLevelResource(player.getHorse().getCurrentLevel())
					.getHorseStats();
			player.getGameStats().replaceModifiers(Horse.PLAYER_LEVEL_STATEID, levelStats, true);
			// player.getGameStats().replaceModifiers(Horse.HORSE_BLESS_STATEID, player.getHorse().getTempBlessStats(), false);
			player.getGameStats().replaceModifiers(Horse.HORSE_ENHANCE, player.getHorse().getEnhanceStats(), true);
			player.getGameStats().replaceModifiers(Horse.HORSE_GROWITEM, player.getHorse().getAllGrowItemStats(), true);
		}
		if (player.isRide()) {
			Stat[] rideStats = res.getSpeedStat()[player.getHorse().getLevel()];
			if (rideStats != null && rideStats.length > 0) {
				player.getGameStats().replaceModifiers(Horse.GAME_STATE_SPEEDID, rideStats, true);
			}
		}
	}

	/**
	 * 激活幻化
	 */
	@Override
	public void activeIllution(Player player, boolean foreverActive, int sign) {
		if (!player.isKingOfking()) {
			throw new ManagedException(ManagedErrorCode.KINGOFKING_NOT);
		}
		Horse horse = player.getHorse();
		HorseAppearance appearce = horse.getAppearance();
		if (appearce.isForeverActive()) {
			throw new ManagedException(ManagedErrorCode.HORSE_ILLUTION_HAS_FOREVER_ACTIVE);
		}

		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				HorseManager.getInstance().getHorseIllutionActiveConditions());
		conditions.verify(player);

		int amount = 0;
		if (true == foreverActive) {
			// 永久激活
			String[] actIds = HorseManager.getInstance().HORSE_FOREVER_ILLUTION_ACT.getValue();
			CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, actIds);
			actions.verify(player, true);
			actions.act(player, ModuleInfo.valueOf(ModuleType.HORSE_ILLUTION, SubModuleType.HORSE_ILLUTION_ACT));
		} else {
			long intervalTime = KingOfWarManager.getInstance().getNextKingOfWarTime() - System.currentTimeMillis();
			// 激活
			int intervalhours = (int) (Math.ceil(intervalTime / DateUtils.MILLIS_PER_HOUR));
			amount = (int) intervalTime;
			// 李廷炫说的 多出1秒就按1小时收费
			if (intervalTime % DateUtils.MILLIS_PER_HOUR >= DateUtils.MILLIS_PER_SECOND) {
				intervalhours++;
			}
			Formula formula = HorseManager.getInstance().HORSE_ILLUTION_ACTIVE_FORMULA;
			// 消耗元宝数
			Integer gold = (Integer) FormulaParmsUtil.valueOf(formula).addParm("n", intervalhours).getValue();
			CurrencyAction currencyActions = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, gold);
			currencyActions.verify(player);
			currencyActions
					.act(player, ModuleInfo.valueOf(ModuleType.HORSE_ILLUTION, SubModuleType.HORSE_ILLUTION_ACT));
		}
		Reward reward = Reward.valueOf();
		// 0 永久激活 1 普通激活
		String code = (true == foreverActive) ? "0" : "1";
		reward.addRewardItem(RewardItem.valueOf(RewardType.HORSE_ILLUTION, code, amount));
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.HORSE_ILLUTION, SubModuleType.HORSE_ILLUTION_REWARD));
		String il18nId = (true == foreverActive) ? "10512" : "10511";
		I18nUtils utils = I18nUtils.valueOf(il18nId);
		utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
		utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, utils, null);

		String chatIl18nId = (true == foreverActive) ? "307004" : "307003";
		I18nUtils chatUtils = I18nUtils.valueOf(chatIl18nId);
		chatUtils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
		chatUtils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(0, chatUtils, null);
		// 返回生效的外观
		PacketSendUtility.sendPacket(player, SM_Illution_Active.valueOf(sign, horse.getAppearance()));
	}

	/**
	 * 取消幻化
	 * 
	 * @param player
	 * @param sign
	 */
	public void cancelActiveIllution(Player player, int sign) {
		Horse horse = player.getHorse();
		HorseAppearance appearance = horse.getAppearance();
		appearance.refreshDeprecated(player);

		if (appearance.isFinishActive() && appearance.getCurrentAppearance() == 0) {
			throw new ManagedException(ManagedErrorCode.HORSE_ILLUTION_IS_NOT_USE);
		}
		appearance.notUseIllution(player);
		PacketSendUtility.sendPacket(player, SM_Cancel_Illution.valueOf(sign, appearance));

	}

	/**
	 * 幻化生效
	 * 
	 * @param player
	 * @param sign
	 */
	public void useIllution(Player player, int sign) {
		Horse horse = player.getHorse();
		HorseAppearance appearance = horse.getAppearance();
		appearance.refreshDeprecated(player);
		if (false == appearance.isFinishActive()) {
			throw new ManagedException(ManagedErrorCode.HORSE_ILLUTION_IS_NOT_ACTIVE);
		}
		if (appearance.getCurrentAppearance() > 0) {
			throw new ManagedException(ManagedErrorCode.HORSE_ILLUTION_IS_USE);
		}

		appearance.useIllution(player);
		PacketSendUtility.sendPacket(player, SM_Illution_Use.valueOf(sign, appearance));
	}

}
