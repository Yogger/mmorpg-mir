package com.mmorpg.mir.model.boss.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.boss.entity.BossEntity;
import com.mmorpg.mir.model.boss.model.BossDropInfo;
import com.mmorpg.mir.model.boss.model.BossHistory;
import com.mmorpg.mir.model.boss.model.BossScheme;
import com.mmorpg.mir.model.boss.model.BossView;
import com.mmorpg.mir.model.boss.packet.SM_Boss_Coins_Buy;
import com.mmorpg.mir.model.boss.packet.SM_Boss_Coins_Upgrade;
import com.mmorpg.mir.model.boss.packet.SM_Boss_Receive_FHReward;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.boss.resource.BossStoreCoinResource;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.drop.model.DropHistory;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class BossManager implements ResourceReload, IBossManager {
	@Static
	private Storage<String, BossResource> bossResources;

	@Inject
	private EntityCacheService<String, BossEntity> bossEntityDbService;

	/** boss计划器 */
	private Map<String, BossScheme> bossSchemes = New.hashMap();

	@Static("BOSS:BOSS_DROPINFO_MAXSIZE")
	private ConfigValue<Integer> BOSS_DROPINFO_MAXSIZE;

	@Static("BOSS:BOSS_NOTITY_BEFORE_SPAWN")
	public ConfigValue<Integer> BOSS_NOTITY_BEFORE_SPAWN;

	@Static("BOSS:BOSS_HP_SITUATION_NOTICE")
	public ConfigValue<Integer[]> BOSS_HP_SITUATION_NOTICE;

	private static BossManager instance;

	@PostConstruct
	public void init() {
		setInstance(this);
	}

	public void onlineGainBossCoins() {
		/*ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				for (Long pid : SessionManager.getInstance().getOnlineIdentities()) {
					Player player = PlayerManager.getInstance().getPlayer(pid);
					if (player != null && isBossCoinModuleOpen(player)) {
						RewardManager.getInstance().grantReward(player, 
								BossConfig.getInstance().COINS_ONLINE_REWARD_ID.getValue(), 
								ModuleInfo.valueOf(ModuleType.BOSS_COINS, SubModuleType.BOSS_COINS_ONLINE_GAIN));
					}
				}
			}
			
		}, BossConfig.getInstance().COINS_ONLINE_GAIN_PERIOD.getValue() * DateUtils.MILLIS_PER_SECOND, 
			BossConfig.getInstance().COINS_ONLINE_GAIN_PERIOD.getValue() * DateUtils.MILLIS_PER_SECOND);*/
	}
	/**
	 * 初始化s所有BOSS
	 */
	public void initAll() {
		reload();
		onlineGainBossCoins();
	}

	@Override
	public void reload() {
		// 清理老的BOSS
		if (!bossSchemes.isEmpty()) {
			for (BossScheme bossScheme : bossSchemes.values()) {
				if (bossScheme != null && bossScheme.getBoss() != null) {
					bossScheme.getBoss().getController().delete();
				}
				bossScheme.cancelRefreshBossFuture();
				bossScheme.cancelRefreshNotifyFuture();
				bossScheme.clearBombstone();
			}
			bossSchemes.clear();
		}

		// 构建新的BossScheme
		for (final BossResource bossResource : bossResources.getAll()) {
			BossHistory bossHistory = loadOrCreateBossEntity(bossResource);
			final BossScheme bossScheme = BossScheme.valueOf(bossResource, bossHistory);
			bossSchemes.put(bossResource.getId(), bossScheme);
			bossScheme.spawnBoss();
		}

	}

	/**
	 * 构建BossHistory
	 * 
	 * @return
	 */
	public BossHistory loadOrCreateBossEntity(BossResource bossResource) {
		BossEntity bossEnt = bossEntityDbService.loadOrCreate(bossResource.getId(),
				new EntityBuilder<String, BossEntity>() {
					@Override
					public BossEntity newInstance(String id) {
						BossEntity ent = new BossEntity();
						ent.setId(id);
						ent.setBossJson(JsonUtils.object2String(new BossHistory()));
						return ent;
					}
				});

		if (bossEnt.getBossHistory() != null) {
			return bossEnt.getBossHistory();
		}

		BossHistory bossHistory = bossEnt.createBossHistroy();
		bossHistory.setBossEntity(bossEnt);
		bossEnt.setBossHistory(bossHistory);
		return bossHistory;
	}

	/**
	 * 获得所有BOSS当前状态
	 * 
	 * @param group
	 * @return
	 */
	public Map<String, BossView> getAllBossStatus(int group) {
		Map<String, BossView> bossViews = New.hashMap();
		for (BossScheme bossScheme : bossSchemes.values()) {
			if (bossScheme.getBossResource().getGroup() == group) {
				bossViews.put(bossScheme.getBossResource().getId(), bossScheme.creartView());
			}
		}
		return bossViews;
	}
	
	/**
	 * 获得单个BOSS当前状态
	 * 
	 * @param group
	 * @return
	 */
	public BossView getBossStatus(String bossId) {
		return bossSchemes.get(bossId).creartView();
	}

	/**
	 * 获取所有掉落信息
	 * 
	 * @return
	 */
	public ArrayList<BossDropInfo> getAllDropInfo() {
		ArrayList<BossDropInfo> allDropInfos = new ArrayList<BossDropInfo>();
		for (BossScheme bossScheme : bossSchemes.values()) {
			List<BossDropInfo> dropInfos = bossScheme.getBossHistory().getDropInfos();
			if (dropInfos != null && !dropInfos.isEmpty()) {
				// 拷贝防止并发异常
				for (BossDropInfo bd : new ArrayList<BossDropInfo>(dropInfos)) {
					bd.setBossScheme(bossScheme);
					allDropInfos.add(bd);
				}
			}
		}
		Collections.sort(allDropInfos);
		if (allDropInfos.size() <= BOSS_DROPINFO_MAXSIZE.getValue()) {
			return allDropInfos;
		} else {
			int dropCount = Math.min(BOSS_DROPINFO_MAXSIZE.getValue(), allDropInfos.size());
			ArrayList<BossDropInfo> lastDropInfos = new ArrayList<BossDropInfo>(allDropInfos.subList(0, dropCount));
			if (allDropInfos.size() > BOSS_DROPINFO_MAXSIZE.getValue() * 2) {
				// 清理,当冗余的个数大于缓存个数2倍时清理
				for (BossDropInfo drop : allDropInfos) {
					if (!lastDropInfos.contains(drop)) {
						drop.delete();
					}
				}
			}
			return lastDropInfos;
		}
	}

	/**
	 * 领取boss首杀奖励
	 * 
	 * @param player
	 * @param bossId
	 */
	public void receiveBossFHReward(Player player, String bossId) {
		DropHistory dropHistory = player.getDropHistory();
		if (!dropHistory.getBossFHRewardHistory().containsKey(bossId)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}

		if (dropHistory.getBossFHRewardHistory().get(bossId)) {
			throw new ManagedException(ManagedErrorCode.BOSS_FHREWARD_HAS_RECEIVE);
		}

		BossResource resource = getBossResource(bossId, true);
		List<String> chooserIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserGroup());
		RewardManager.getInstance().grantReward(player, chooserIds,
				ModuleInfo.valueOf(ModuleType.BOSS_FB, SubModuleType.BOSS_FIRST));
		dropHistory.getBossFHRewardHistory().put(bossId, true);
		PacketSendUtility.sendPacket(player, SM_Boss_Receive_FHReward.valueOf(bossId));
	}

	public void updateBossHistory(BossHistory boss) {
		bossEntityDbService.writeBack(boss.getBossEntity().getId(), boss.getBossEntity());
	}

	public static BossManager getInstance() {
		return instance;
	}

	public BossResource getBossResource(String key, boolean throwException) {
		return bossResources.get(key, throwException);
	}

	public static void setInstance(BossManager instance) {
		BossManager.instance = instance;
	}

	@Override
	public Class<?> getResourceClass() {
		return BossResource.class;
	}

	public void bosscoinModuleOpen(ModuleOpenEvent event) {
		if (event.getModuleResourceId().equals(BossConfig.getInstance().COINS_OPEN_MODULE_ID.getValue())) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			player.getBossData().openModule();
		}
	}

	public void upgradeBossCoinsLevel(Player player) {
		if (!isBossCoinModuleOpen(player)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			return;
		}
		if ((player.getBossData().getBossCoinsLevel() == null || player.getBossData().getBossCoinsLevel().isEmpty())) {
			player.getBossData().openModule();
		}
		BossStoreCoinResource selfId = BossConfig.getInstance().getBossStoreCoinResource(player.getBossData().getBossCoinsLevel());
		if (!selfId.getUpgradeCoreActions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (selfId.getNextId() == null || selfId.getNextId().isEmpty()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.BOSS_COINS_MAX_LEVEL);
			return;
		}
		String iterId = selfId.getId();
		CoreActions actions = new CoreActions();
		CoreActions actActions = new CoreActions();
		while (iterId != null && !iterId.isEmpty()) {
			BossStoreCoinResource res = BossConfig.getInstance().getBossStoreCoinResource(iterId);
			actions.addActions(res.getUpgradeCoreActions());
			if (actions.verify(player, false) && (res.getNextId() != null && !res.getNextId().isEmpty())) {
				actActions.addActions(res.getUpgradeCoreActions());
				iterId =  res.getNextId();
			} else {
				break;
			}
		}
		if (iterId.equals(selfId.getId())) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.BOSS_COINS_NOT_ENOUGH);
			return;
		}
		actActions.act(player, ModuleInfo.valueOf(ModuleType.BOSS_COINS, SubModuleType.BOSS_COINS_UPGRADE_LEVEL));
		player.getBossData().setBossCoinsLevel(iterId);
		player.getBossData().updatesStats(true);
		PacketSendUtility.sendPacket(player, SM_Boss_Coins_Upgrade.valueOf(player));
	}

	public void buyBossCoinsExtraStats(Player player, String id) {
		
		if (!isBossCoinModuleOpen(player)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			return;
		}
		if (player.getBossData().getPayedBossCoinsIds().contains(id)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.BOSS_COINS_ALREADY_PAYED);
			return;
		}
		BossStoreCoinResource buyId = BossConfig.getInstance().getBossStoreCoinResource(id);
		BossStoreCoinResource selfId = BossConfig.getInstance().getBossStoreCoinResource(player.getBossData().getBossCoinsLevel());
		boolean isSatisfy = selfId.getQuality() > buyId.getQuality() || (selfId.getQuality() == buyId.getQuality() && selfId.getLevel() >= buyId.getLevel());
		if (!isSatisfy) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.BOSS_COINS_CANNOT_BUY);
			return;
		}
		if (buyId.getPayedStats() == null || buyId.getPayedStats().length == 0) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.BOSS_COINS_CANNOT_BUY);
			return;
		}
		if (!buyId.getBuyCoreActions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.BOSS_COINS_NOT_ENOUGH);
			return;
		}
		buyId.getBuyCoreActions().act(player, ModuleInfo.valueOf(ModuleType.BOSS_COINS, SubModuleType.BOSS_COINS_BUY_STATS, id));
		player.getBossData().getPayedBossCoinsIds().add(id);
		player.getBossData().updatesStats(true);
		PacketSendUtility.sendPacket(player, SM_Boss_Coins_Buy.valueOf(player));
	}

	public boolean isBossCoinModuleOpen(Player player) {
		String moduleId = BossConfig.getInstance().COINS_OPEN_MODULE_ID.getValue();
		return ModuleOpenManager.getInstance().isOpenByKey(player, moduleId);
	}
}
