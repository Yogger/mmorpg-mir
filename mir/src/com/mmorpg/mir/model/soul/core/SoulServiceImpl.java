package com.mmorpg.mir.model.soul.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.soul.event.SoulUpgradeEvent;
import com.mmorpg.mir.model.soul.model.Soul;
import com.mmorpg.mir.model.soul.packet.SM_Soul_Uplevel;
import com.mmorpg.mir.model.soul.packet.SM_Soul_Uplevel_Notify_Others;
import com.mmorpg.mir.model.soul.resource.SoulResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

/**
 * 英魂数据管理
 * 
 * @author 37wan
 * 
 */
@Component
public class SoulServiceImpl implements SoulService {

	@Autowired
	private SoulManager soulManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Autowired
	private ChooserManager chooserManager;

	/**
	 * 登陆算属性
	 */
	public void initSoulStats(Player player) {
		// 计算基本属性
		if (soulManager.isOpen(player)) {
			flushSoul(player, false, false);
		}
	}

	private void clearTime(Player player) {
		// 超过24小时清除当前的祝福值
		boolean isCountReset = SoulManager.getInstance().getSoulResource(player.getSoul().getLevel()).isCountReset();
		if (isCountReset && player.getSoul().isTimeOut()) {
			player.getSoul().setUpSum(0);
			player.getSoul().setNowBlessValue(0);
			player.getSoul().intervalClearTime(System.currentTimeMillis());
		}
	}

	// 老服用户
	public void doOldUpgrade(Player player) {
		Soul soul = player.getSoul();

		List<SoulResource> soulStorage = new ArrayList<SoulResource>(soulManager.soulStorage.getAll());
		Collections.sort(soulStorage, new SoulResource.GradeComparator());

		int grade = soul.getLevel();
		int level = soul.getRank();
		int upCount = soul.getUpSum();
		for (SoulResource resource : soulStorage) {
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
		if (grade != soul.getLevel() || level != soul.getRank()) {
			soul.setLevel(grade);
			soul.setRank(level);
			soul.setUpSum(upCount);
			LogManager.soulUp(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(),
					player.getName(), player.getObjectId(), player.getSoul().getLevel(), player.getSoul().getRank(),
					player.getSoul().getUpSum(), System.currentTimeMillis());
			// 老玩家修改
			flushSoul(player, true, false);
		}
	}

	public SM_Soul_Uplevel uplevel(Player player, boolean autoBuy) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		// 系统未开启
		if (!soulManager.isOpen(player)) {
			throw new ManagedException(ManagedErrorCode.SOUL_NOT_OPEN);
		}

		Soul soul = player.getSoul();
		if (soul.isMaxLevel() && soul.isMaxGrade()) {
			throw new ManagedException(ManagedErrorCode.SOUL_MAX_LEVEL);
		}
		// 老用户
		doOldUpgrade(player);
		clearTime(player);

		SoulResource res = soulManager.getSoulResource(player.getSoul().getLevel());

		// 计算消耗
		CoreActions actions = new CoreActions();
		actions.addActions(coreActionManager.getCoreActions(1, res.getCopperActions()));

		// 先计算优先道具，先消耗 限时道具，不够再消耗剩余所需份数道具，勾选自动购买，不足再消耗剩余所需份数的元宝
		int criItemCount = 0;
		// 周暴击活动
		boolean canWeekCri = false;
		if (res.getMaterialActionFirstPri() != null && res.getMaterialActionFirstPri().length != 0) {
			// 暴击丹
			String criItemId = coreActionManager.getCoreActions(1, res.getMaterialActionFirstPri()).getFirstItemKey();
			criItemCount = (int) player.getPack().getItemSizeByKey(criItemId);
		}

		if (res.getCount() <= criItemCount) {
			// 优先扣除暴击丹
			canWeekCri = true;
			CoreActions priActions = coreActionManager.getCoreActions(res.getCount(), res.getMaterialActionFirstPri());
			actions.addActions(priActions);
		} else {
			int needItemCount = res.getCount() - criItemCount;
			if (criItemCount > 0) {
				// 扣除暴击丹
				actions.addActions(coreActionManager.getCoreActions(criItemCount, res.getMaterialActionFirstPri()));
			}
			int priItemCount = 0;
			if (res.getMaterialActionsPriority() != null && res.getMaterialActionsPriority().length != 0) {
				//
				String itemId = coreActionManager.getCoreActions(1, res.getMaterialActionsPriority()).getFirstItemKey();
				priItemCount = (int) player.getPack().getItemSizeByKey(itemId);
			}
			if (needItemCount <= priItemCount) {
				actions.addActions(coreActionManager.getCoreActions(needItemCount, res.getMaterialActionsPriority()));
			} else {
				if (priItemCount > 0) {
					actions.addActions(coreActionManager.getCoreActions(priItemCount, res.getMaterialActionsPriority()));
				}
				needItemCount = needItemCount - priItemCount;
				CoreActions itemActions = coreActionManager.getCoreActions(needItemCount, res.getMaterialActions());
				String trueItemId = itemActions.getFirstItemKey();
				int itemHave = (int) player.getPack().getItemSizeByKey(trueItemId);
				if (needItemCount > itemHave && !autoBuy) {
					throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
				} else {
					int needGold = needItemCount - itemHave;
					if (needGold <= 0) {
						actions.addActions(coreActionManager.getCoreActions(needItemCount, res.getMaterialActions()));
					} else {
						if (itemHave > 0) {
							actions.addActions(coreActionManager.getCoreActions(itemHave, res.getMaterialActions()));
						}
						actions.addActions(coreActionManager.getCoreActions(needGold, res.getGoldActions()));
					}
				}
			}
		}

		// 真正扣除物品,铜钱,并做日志记录
		actions.verify(player, true);
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.SOUL, SubModuleType.SOUL_UPGRADE);
		actions.act(player, moduleInfo);

		int addCount = 1;
		if (soulManager.getCriActivityConds().verify(player)) {
			String value = chooserManager.chooseValueByRequire(player,
					soulManager.CRI_ACTIVITY_CHOSERGROUPID.getValue()).get(0);
			addCount = Integer.parseInt(value);
		}
		// 周暴击活动，跟上面的不冲突
		if (canWeekCri) {
			String value = chooserManager.chooseValueByRequire(player, soulManager.WEEK_CRI_CHOOSER_ID.getValue()).get(
					0);
			addCount = Integer.parseInt(value);
		}

		soul.addUpSum(addCount);
		SoulResource resource = soulManager.getSoulResource(soul.getLevel());
		int needCount = resource.getNeedCount()[soul.getRank()];
		if (soul.getUpSum() >= needCount) {
			soul.upgrade();
			flushSoul(player, true, true);
		}

		String chatIl18n = "60105";
		String tvIl18n = "307013";
		if (canWeekCri) {
			chatIl18n = "60108";
			tvIl18n = "307016";
		}
		broadCastCriActivity(player, addCount, chatIl18n, tvIl18n);
		SM_Soul_Uplevel sm = SM_Soul_Uplevel.valueOf(player.getSoul());
		sm.setActions(actions);
		sm.setAddCount(addCount);
		return sm;
	}

	/*
	 * public void useBlessItem(Player player, int blessValue) {
	 * clearTime(player); // 等级满了 if (player.getSoul().isMaxLevel()) { throw new
	 * ManagedException(ManagedErrorCode.SOUL_MAX_LEVEL); } Soul soul =
	 * player.getSoul(); soul.addUpSum(blessValue /
	 * soulManager.BLESSVALUE_CONVER_COUNT.getValue()); doUpgrade(player,
	 * blessValue); SM_Soul_Uplevel sm =
	 * SM_Soul_Uplevel.valueOf(player.getSoul());
	 * PacketSendUtility.sendPacket(player, sm); }
	 */

	private void broadCastCriActivity(Player player, int addCount, String chatIl18n, String tvI18n) {
		if (soulManager.BROADCAST_LEAST_RATE.getValue() <= addCount) {
			I18nUtils utils = I18nUtils.valueOf(chatIl18n);
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm("n", I18nPack.valueOf(addCount + ""));
			ChatManager.getInstance().sendSystem(11001, utils, null);

			// tv
			I18nUtils tvUtils = I18nUtils.valueOf(tvI18n);
			tvUtils.addParm("name", I18nPack.valueOf(player.getName()));
			tvUtils.addParm("n", I18nPack.valueOf(addCount + ""));
			ChatManager.getInstance().sendSystem(6, tvUtils, null, player.getCountry());
		}

	}

	/**
	 * 玩家进阶
	 * 
	 * @param player
	 */
	public void flushSoul(Player player, boolean upLevel, boolean clear) {
		addStats(player);
		if (upLevel) {
			// 通知周围玩家
			PacketSendUtility.broadcastPacket(player, SM_Soul_Uplevel_Notify_Others.valueOf(player), true);
			Soul soul = player.getSoul();
			if (clear) {
				soul.clear();
			}
			EventBusManager.getInstance().submit(SoulUpgradeEvent.valueOf(player));
			player.getGameStats().recomputeStats();
		}
	}

	/**
	 * 加上玩家的属性
	 * 
	 * @param player
	 */
	private void addStats(Player player) {
		SoulResource res = soulManager.getSoulResource(player.getSoul().getLevel());
		Stat[] stats = res.getStats()[player.getSoul().getRank()];// 奖励属性
		if (stats != null && stats.length > 0) {
			player.getGameStats().endModifiers(Soul.SOUL_STAT_ID, false);
			player.getGameStats().addModifiers(Soul.SOUL_STAT_ID, stats, false);
		}
		player.getGameStats().replaceModifiers(Soul.SOUL_ENHANCE, player.getSoul().getEnhanceStats(), false);
		player.getGameStats().replaceModifiers(Soul.SOUL_GROW, player.getSoul().getAllGrowItemStats(), false);
		// player.getGameStats().replaceModifiers(Soul.SOUL_BLESS, player.getSoul().getTempBlessStats(), false);
	}

}
