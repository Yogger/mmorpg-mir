package com.mmorpg.mir.model.artifact.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.artifact.event.ArtifactUpEvent;
import com.mmorpg.mir.model.artifact.model.Artifact;
import com.mmorpg.mir.model.artifact.packet.SM_Artifact_Uplevel;
import com.mmorpg.mir.model.artifact.packet.SM_Artifact_Uplevel_Notify_Others;
import com.mmorpg.mir.model.artifact.resource.ArtifactResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

/**
 * 神兵逻辑操作
 * 
 * @author 37wan
 * 
 */
@Component
public class ArtifactServiceImpl implements ArtifactService {

	@Autowired
	private ArtifactManager artifactManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Autowired
	private ChooserManager chooserManager;

	/**
	 * 登陆通知
	 */
	public void initArtifactStats(Player player) {
		// 计算基本属性
		if (artifactManager.isOpen(player)) {
			addStats(player);
		}
		addBuffStates(player);
	}

	private void clearTime(Player player) {
		// 超过24小时清除当前的祝福值
		boolean isBlessReset = ArtifactManager.getInstance().getArtifactResource(player.getArtifact().getLevel())
				.isCountReset();
		if (isBlessReset && player.getArtifact().isTimeOut(artifactManager.CLEAR_BLESSVALUE_INTERVAL_HOUR.getValue())) {
			player.getArtifact().setUpSum(0);
			player.getArtifact().setNowBlessValue(0);
			player.getArtifact().intervalClearTime(System.currentTimeMillis(),
					artifactManager.CLEAR_BLESSVALUE_INTERVAL_HOUR.getValue());
		}
	}

	// 老服用户
	public void doOldUpgrade(Player player) {
		Artifact artifact = player.getArtifact();

		List<ArtifactResource> artifactStorage = new ArrayList<ArtifactResource>(
				artifactManager.artifactStorage.getAll());
		Collections.sort(artifactStorage, new ArtifactResource.GradeComparator());

		int grade = artifact.getLevel();
		int level = artifact.getRank();
		int upCount = artifact.getUpSum();
		for (ArtifactResource resource : artifactStorage) {
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
		if (grade != artifact.getLevel() || level != artifact.getRank()) {
			artifact.setLevel(grade);
			artifact.setRank(level);
			artifact.setUpSum(upCount);
			LogManager.artifactUp(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(), player
					.getName(), player.getObjectId(), player.getArtifact().getLevel(), player.getArtifact().getRank(),
					player.getArtifact().getUpSum(), System.currentTimeMillis());
			// 老玩家修改
			flushAritfact(player, true, false);
		}
	}

	public void buyArtifactBuff(Player player) {
		CoreActions coreActions = artifactManager.getBuffBuyActions();
		if (!coreActions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ERROR_MSG);
			return;
		}
		coreActions.act(player, ModuleInfo.valueOf(ModuleType.USEITEM, SubModuleType.ARTIFACT_BUFF_BUY));
		RewardManager.getInstance().grantReward(player, artifactManager.BUFF_BUY_REWARDID.getValue(),
				ModuleInfo.valueOf(ModuleType.ARTIFACT, SubModuleType.ARTIFACT_BUFF_REWARD));
	}

	public SM_Artifact_Uplevel uplevel(Player player, boolean autoBuy) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		// 系统未开启
		if (!artifactManager.isOpen(player)) {
			throw new ManagedException(ManagedErrorCode.ARTIFACT_NOT_OPEN);
		}

		Artifact artifact = player.getArtifact();

		// 等级满了
		if (artifact.isMaxGrade() && artifact.isMaxRank()) {
			throw new ManagedException(ManagedErrorCode.ARTIFACT_MAX_LEVEL);
		}

		// 处理老用户
		doOldUpgrade(player);
		clearTime(player);

		ArtifactResource res = artifactManager.getArtifactResource(player.getArtifact().getLevel());

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
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.ARTIFACT, SubModuleType.ARTIFACT_UPGRADE);
		actions.act(player, moduleInfo);

		int addCount = 1;
		if (artifactManager.getCriActivityConds().verify(player)) {
			String value = chooserManager.chooseValueByRequire(player,
					artifactManager.CRI_ACTIVITY_CHOSERGROUPID.getValue()).get(0);
			addCount = Integer.parseInt(value);
		}
		// 周暴击活动，跟上面的不冲突
		if (canWeekCri) {
			String value = chooserManager.chooseValueByRequire(player, artifactManager.WEEK_CRI_CHOOSER_ID.getValue())
					.get(0);
			addCount = Integer.parseInt(value);
		}
		artifact.addUpSum(addCount);
		ArtifactResource resource = artifactManager.getArtifactResource(artifact.getLevel());
		int needCount = resource.getNeedCount()[artifact.getRank()];

		if (artifact.getUpSum() >= needCount) {
			// 升级
			artifact.upgrade();
			flushAritfact(player, true, true);
		}

		String chatIl18n = "60104";
		String tvIl18n = "307012";
		if (canWeekCri) {
			chatIl18n = "60107";
			tvIl18n = "307015";
		}
		broadCastCriActivity(player, addCount, chatIl18n, tvIl18n);
		SM_Artifact_Uplevel sm = SM_Artifact_Uplevel.valueOf(player.getArtifact());
		sm.setActions(actions);
		sm.setAddCount(addCount);
		return sm;
	}

	private void broadCastCriActivity(Player player, int addCount, String chatIl18n, String tvIl18n) {
		if (artifactManager.BROADCAST_LEAST_RATE.getValue() <= addCount) {
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

	/*
	 * public void useBlessItem(Player player, int blessValue) { // 系统未开启 if
	 * (!artifactManager.isOpen(player)) { throw new
	 * ManagedException(ManagedErrorCode.ARTIFACT_NOT_OPEN); }
	 * 
	 * Artifact artifact = player.getArtifact();
	 * 
	 * // 等级满了 if (artifact.isMaxLevel()) { throw new
	 * ManagedException(ManagedErrorCode.ARTIFACT_MAX_LEVEL); }
	 * 
	 * clearTime(player);
	 * 
	 * artifact.addUpSum(blessValue /
	 * artifactManager.BLESSVALUE_CONVER_COUNT.getValue()); ArtifactResource res
	 * = artifactManager.getArtifactResource(artifact.getLevel()); //
	 * 进阶次数超过最大次数一定成功 if (artifact.getUpSum() >= res.getMaxSum()) {
	 * addGrade(player); flushAritfact(player, true); } else if
	 * (artifact.getUpSum() <= res.getMinSum()) { // 进阶次数小于最小次数,必定失败 //
	 * 失败后增加当前祝福值 addBlessValue(player, blessValue); } else { //
	 * 进阶次数在最小和最大之间,计算概率 // 计算进阶概率 if (probPass(player, res)) {
	 * player.getArtifact().addLevel(); flushAritfact(player, true); } else { //
	 * 未达到进阶概率增加祝福值 addBlessValue(player, blessValue); } } SM_Artifact_Uplevel
	 * sm = SM_Artifact_Uplevel.valueOf(player.getArtifact());
	 * PacketSendUtility.sendPacket(player, sm); }
	 */

	/**
	 * 玩家进阶
	 * 
	 * @param player
	 */
	public void flushAritfact(Player player, boolean upLevel, boolean clear) {
		addStats(player);
		// 通知周围玩家
		if (upLevel) {
			PacketSendUtility.broadcastPacket(player, SM_Artifact_Uplevel_Notify_Others.valueOf(player), true);
			Artifact artifact = player.getArtifact();
			if (clear) {
				artifact.clear();
			}
			EventBusManager.getInstance().submit(ArtifactUpEvent.valueOf(player));
			// 刷新人物属性
			player.getGameStats().recomputeStats();
		}
	}

	/**
	 * 加上玩家的属性
	 * 
	 * @param player
	 */
	public void addStats(Player player) {
		ArtifactResource res = artifactManager.getArtifactResource(player.getArtifact().getLevel());
		Stat[] stats = res.getStats()[player.getArtifact().getRank()];// 奖励属性
		if (stats != null && stats.length > 0) {
			player.getGameStats().endModifiers(Artifact.Artifact_STAT_ID, false);
			player.getGameStats().addModifiers(Artifact.Artifact_STAT_ID, stats, false);
			player.getGameStats().replaceModifiers(Artifact.ARTIFACT_ENHANCE, player.getArtifact().getEnhanceStats(),
					false);
			player.getGameStats().replaceModifiers(Artifact.ARTIFACT_GROW, player.getArtifact().getAllGrowItemStats(),
					false);
			// player.getGameStats().replaceModifiers(Artifact.ARTIFACT_BLESS, player.getArtifact().getTempBlessStats(), false);
		}
	}

	/*
	 * 登陆的时候计算属性
	 */
	public void addBuffStates(Player player) {
		if (player.getArtifact().getBuffEndTime() > System.currentTimeMillis()) {
			Stat[] stats = artifactManager.BUFF_STATS.getValue();
			player.getGameStats().endModifiers(Artifact.Artifact_BUFF_ID, false);
			player.getGameStats().addModifiers(Artifact.Artifact_BUFF_ID, stats, false);
		}
	}

	@Override
	public void deprechArtifactBuff(Player player) {
		player.getArtifact().checkBuffDeprect();
	}

}
