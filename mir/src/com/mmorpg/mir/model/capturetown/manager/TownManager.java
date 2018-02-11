package com.mmorpg.mir.model.capturetown.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.capturetown.entity.CaptureTownEnt;
import com.mmorpg.mir.model.capturetown.model.CaptureInfoType;
import com.mmorpg.mir.model.capturetown.model.ResultType;
import com.mmorpg.mir.model.capturetown.model.TownLog;
import com.mmorpg.mir.model.capturetown.model.TownSchema;
import com.mmorpg.mir.model.capturetown.model.TownType;
import com.mmorpg.mir.model.capturetown.packet.SM_Buy_Motion;
import com.mmorpg.mir.model.capturetown.packet.SM_Get_Country_Fight_Info;
import com.mmorpg.mir.model.capturetown.packet.SM_Get_My_Fight_Info;
import com.mmorpg.mir.model.capturetown.packet.SM_Get_Towns_Info;
import com.mmorpg.mir.model.capturetown.packet.SM_Reset_PlayerTownInfo;
import com.mmorpg.mir.model.capturetown.packet.SM_Town_Copy_Finish;
import com.mmorpg.mir.model.capturetown.resource.TownResource;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.model.CopyInfo;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.service.HorseService;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.MailGroup;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.service.MapInstanceService;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

/**
 * @author 37wan
 * 
 */
@Component
public class TownManager {

	@Autowired
	private TownConfig townConfig;

	@Autowired
	private HorseService horseService;

	@Inject
	private EntityCacheService<Integer, CaptureTownEnt> captureTownEntDbService;

	private CaptureTownEnt ent;

	private TownSchema townSchema;

	private static TownManager townManager;

	public static TownManager getInstance() {
		return townManager;
	}

	@PostConstruct
	void init() {
		if (ClearAndMigrate.clear) {
			return;
		}
		townManager = this;
		initAllTown();
	}

	private void initAllTown() {
		ent = captureTownEntDbService.loadOrCreate(1, new EntityBuilder<Integer, CaptureTownEnt>() {
			@Override
			public CaptureTownEnt newInstance(Integer id) {
				CaptureTownEnt ent = new CaptureTownEnt();
				ent.setId(id);
				TownSchema schema = new TownSchema();
				for (TownResource resource : townConfig.townResources.getAll()) {
					if (resource.getType() == TownType.PVE) {
						continue;
					}
					schema.initANewTown(resource.getId());
				}
				TownLog log = new TownLog();
				schema.setTownLogger(log);
				ent.setTownInfoJson(JsonUtils.object2String(schema));
				return ent;
			}
		});

		townSchema = ent.getTownSchema();
		townSchema.initAllTown();
	}

	/**
	 * 更新城池信息
	 */
	public void updateCaptureTownEnt() {
		captureTownEntDbService.writeBack(1, ent);
	}

	public void rewardAndReset() {
		Map<Integer, Integer> rankMap = townSchema.getCountryCaptureRank();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			// refresh Online player
			for (Player player : country.getCivils().values()) {
				player.getPlayerCountryHistory().getCaptureTownInfo().refresh();
				PacketSendUtility.sendPacket(player, SM_Reset_PlayerTownInfo.valueOf(player));
			}
			// send mail
			Integer rank = rankMap.get(country.getId().getValue());
			Reward reward = townConfig.getCountryRankReward(rank);
			Date nextTime = DateUtils.getNextTime(townConfig.getResetAndRewardTime(), new Date());
			MailGroup mailGroup = MailGroup.valueOf(townConfig.getCountryRankMailTitle(rank),
					townConfig.getCountryRankMailContent(rank), null,
					townConfig.getTownModuleOpenCoreConditionResource(country.getId().getValue()), reward,
					nextTime.getTime());

			MailManager.getInstance().addMailGroup(mailGroup);
			for (long playerId : SessionManager.getInstance().getOnlineIdentities()) {
				MailManager.getInstance().receiveGroupMail(playerId);
			}
		}
	}

	public long challengeTownOwner(final Player player, final String targetTownKey) {
		TownResource resource = townConfig.getTownResource(targetTownKey);
		final long targetPlayerId = townSchema.getTownOwnerPlayerId(targetTownKey);
		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		if (player.isInCopy()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		if (!townConfig.getTownModuleOpenCond().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		if (!townConfig.getTownChallenegeConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		if (targetTownKey == null || resource.getType() == TownType.PVE
				|| targetTownKey.equals(player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey())) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (targetPlayerId == 0L) {
			throw new ManagedException(ManagedErrorCode.TOWN_ALREADY_EMPTY);
		}
		if (player.getPlayerCountryHistory().getCaptureTownInfo().getDailyCount() >= townConfig.PLAYER_ENTER_DAILY_LIMIT
				.getValue()) {
			throw new ManagedException(ManagedErrorCode.CAPTURE_MOTION_NOT_ENOUGH);
		}
		if (!player.getPlayerCountryHistory().getCaptureTownInfo().canEnter()) {
			throw new ManagedException(ManagedErrorCode.TOWN_CHALLENGE_CD);
		}
		Player targetPlayer = PlayerManager.getInstance().getTargetPlayer(targetPlayerId);
		if (targetPlayer.getCountryValue() == player.getCountryValue()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_SAME);
		}
		constructCopy(player, targetTownKey, targetPlayer, resource);

		horseService.unRide(player);
		player.getRp().clearRpBuff();
		player.getPlayerCountryHistory().getCaptureTownInfo().addDailyCount();
		return player.getPlayerCountryHistory().getCaptureTownInfo().enterAccTimeCD();
	}

	private void constructCopy(final Player player, final String targetTownKey, final Player targetPlayer,
			TownResource resource) {
		// 构建副本
		final WorldMapInstance worldMapInstance = MapInstanceService.createCommonMapCopy(resource.getMapId(), false);
		player.getMoveController().stopMoving();
		player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		final CopyInfo copyInfo = worldMapInstance.getCopyInfo();
		copyInfo.setCopyId(targetTownKey);
		copyInfo.setMapId(resource.getMapId());
		copyInfo.setCreateTime(worldMapInstance.getCreateTime());
		copyInfo.setTargetPlayerId(targetPlayer.getObjectId());
		worldMapInstance.register(player.getObjectId());

		final Creature robotNpc = (Creature) SpawnManager.getInstance().creatObject(
				resource.getDefendNpcSpawnKey()[targetPlayer.getRole() - 1], worldMapInstance.getInstanceId(),
				targetPlayer, resource.getStatsFactor());
		targetPlayer.getCollect().getFamedGeneral().refreshLearnSkill(robotNpc, targetPlayer);
		final AtomicBoolean onceShot = new AtomicBoolean();
		final Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				try {
					if (!onceShot.compareAndSet(false, true)) {
						return;
					}

					copyInfo.setReward(true);
					robotNpc.getController().delete();
					townSchema.addSelfLog(player, CaptureInfoType.ATTACK_FAIL, targetPlayer, 0, null, targetTownKey);
					townSchema.addSelfLog(targetPlayer, CaptureInfoType.BEENATTACKED_FAIL, player, 0, targetTownKey,
							null);
					if (townConfig.isInTownsCopyMap(player.getMapId())) {
						PacketSendUtility.sendPacket(player, SM_Town_Copy_Finish.valueOf(
								ResultType.FAIL_TIME_OUT.getValue(), targetPlayer, 0, targetTownKey));
					}
				} catch (Exception e) {

				}
			}
		}, resource.getChallengeMaxMin() * DateUtils.MILLIS_PER_MINUTE);
		robotNpc.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				if (!onceShot.compareAndSet(false, true)) {
					return;
				}
				if (future != null && !future.isCancelled()) {
					future.cancel(false);
				}
				copyInfo.setReward(true);
				if (player.getLifeStats().isAlreadyDead()) {
					townSchema.addSelfLog(player, CaptureInfoType.ATTACK_FAIL, targetPlayer, 0, null, targetTownKey);
					townSchema.addSelfLog(targetPlayer, CaptureInfoType.BEENATTACKED_FAIL, player, 0, targetTownKey,
							null);
					PacketSendUtility.sendPacket(player, SM_Town_Copy_Finish.valueOf(
							ResultType.FAIL_BEEN_KILL.getValue(), targetPlayer, 0, targetTownKey));
				} else {
					SM_Town_Copy_Finish pack = townSchema.captureTargetTown(player, targetTownKey, targetPlayer);
					PacketSendUtility.sendPacket(player, pack);
				}
			}
		});
		player.getObserveController().attach(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				if (!onceShot.compareAndSet(false, true)) {
					return;
				}
				if (!townConfig.isInTownsCopyMap(player.getMapId())) {
					return;
				}
				if (robotNpc.getLifeStats().isAlreadyDead()) {
					return;
				}
				robotNpc.getController().delete();
				if (future != null && !future.isCancelled()) {
					future.cancel(false);
				}
				copyInfo.setReward(true);
				townSchema.addSelfLog(player, CaptureInfoType.ATTACK_FAIL, targetPlayer, 0, null, targetTownKey);
				townSchema.addSelfLog(targetPlayer, CaptureInfoType.BEENATTACKED_FAIL, player, 0, targetTownKey, null);
				PacketSendUtility.sendPacket(player, SM_Town_Copy_Finish.valueOf(ResultType.FAIL_BEEN_KILL.getValue(),
						targetPlayer, 0, targetTownKey));
			}
		});
		worldMapInstance.getTriggerTasks().add(future);
		worldMapInstance.getCallbackFutures().put(TownType.PVP.name(), future);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				SpawnManager.getInstance().bringIntoWorld(robotNpc, worldMapInstance.getInstanceId());
			}
		}, 5000);

		MapInstanceService.startInstanceChecker(worldMapInstance);
		player.getLifeStats().fullStoreHpAndMp();
		World.getInstance().setPosition(player, worldMapInstance.getParent().getMapId(),
				worldMapInstance.getInstanceId(), resource.getX(), resource.getY(), resource.getHeading());
		player.sendUpdatePosition();
	}

	public void leaveTownCopy(Player player, boolean throwException) {
		// check
		if (!townConfig.isInTownsCopyMap(player.getMapId())) {
			if (throwException) {
				throw new ManagedException(ManagedErrorCode.PLAYER_NOT_IN_POSITION);
			} else {
				return;
			}
		}
		WorldMapInstance mapInstance = MapInstanceService
				.getRegisteredInstance(player.getMapId(), player.getObjectId());
		if (!mapInstance.getCopyInfo().isReward() && mapInstance.getCopyInfo().getTargetPlayerId() != 0L) {
			Player targetPlayer = PlayerManager.getInstance().getPlayer(mapInstance.getCopyInfo().getTargetPlayerId());
			townSchema.addSelfLog(player, CaptureInfoType.ATTACK_FAIL, targetPlayer, 0, null, mapInstance.getCopyInfo()
					.getCopyId());
			townSchema.addSelfLog(targetPlayer, CaptureInfoType.BEENATTACKED_FAIL, player, 0, mapInstance.getCopyInfo()
					.getCopyId(), null);
		}
		MapInstanceService.destroyInstance(mapInstance);
		player.getMoveController().stopMoving();

		// 回家
		if (player.getCopyHistory().getRouteStep() != null) {
			RouteStep loc = player.getCopyHistory().getRouteStep();
			WorldPosition position = World.getInstance().createPosition(loc.getMapId(), 1, loc.getX(), loc.getY(),
					(byte) 0);
			player.setPosition(position);
		} else {
			// 避免出错，让他回新手村
			List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryValue(),
					ConfigValueManager.getInstance().BIRTH_POINT.getValue());
			RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
			WorldPosition position = World.getInstance().createPosition(p.getMapId(), 1, p.getX(), p.getY(), (byte) 0);
			player.setPosition(position);
		}
		if (player.getLifeStats().isAlreadyDead()) {
			player.getLifeStats().fullStoreHpAndMp();
		}
		player.sendUpdatePosition();
	}

	public void getTownsInfo(Player player) {
		PacketSendUtility.sendPacket(player, SM_Get_Towns_Info.valueOf(player));
	}

	public void getTargetTownInfo(Player player, String townKey) {
		PacketSendUtility.sendPacket(player, townSchema.getSpecifiedTownInfo(townKey));
	}

	public int recievedTownReward(Player player) {
		// check owner
		if (!townConfig.getTownModuleOpenCond().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		String key = player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
		if (townConfig.getTownResource(key).getType() == TownType.PVE) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getPlayerCountryHistory().getCaptureTownInfo().getDailyCount() >= townConfig.PLAYER_ENTER_DAILY_LIMIT
				.getValue()) {
			throw new ManagedException(ManagedErrorCode.CAPTURE_MOTION_NOT_ENOUGH);
		}

		int feats = townSchema.recievedTownReward(player);
		if (feats == 0) {
			throw new ManagedException(ManagedErrorCode.TOWN_BEEN_ROBBED);
		}
		return feats;
	}

	public SM_Buy_Motion buyMotion(Player player) {
		CurrencyAction actions = townConfig.getAddCountGoldActionValue(player);
		if (!actions.verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.CAPTURE_TOWN, SubModuleType.TOWN_BUY_COUNT));
		player.getPlayerCountryHistory().getCaptureTownInfo().buyDailyCount();
		return SM_Buy_Motion.valueOf(player);
	}

	public void clearCDByGold(Player player) {
		long nextChallengeTime = player.getPlayerCountryHistory().getCaptureTownInfo().getNextChallengeTime();
		boolean duringCD = nextChallengeTime != 0 && nextChallengeTime > System.currentTimeMillis();
		if (!duringCD) {
			throw new ManagedException(ManagedErrorCode.CAPTURE_NOT_IN_CD);
		}
		CurrencyAction actions = townConfig.getClearCDGoldActionValue(player);
		if (!actions.verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.CAPTURE_TOWN, SubModuleType.TOWN_RESET_CHANLLENGE_CD));
		player.getPlayerCountryHistory().getCaptureTownInfo().clearCDCount();
	}

	public void occupyPveTown(Player player) {
		// maybe send something
		if (!townConfig.getTownModuleOpenCond().verify(player, false)) {
			return;
		}
		player.getPlayerCountryHistory().getCaptureTownInfo().startAutoRewardTask(false);
		player.getPlayerCountryHistory().getCaptureTownInfo().setLastResetTime(System.currentTimeMillis());
		player.getPlayerCountryHistory().getCaptureTownInfo().setLastDailyResetTime(System.currentTimeMillis());
	}

	public long getTownAccFeats(String key) {
		if (key == null || townConfig.getTownResource(key).getType() == TownType.PVE) {
			return 0l;
		}
		return townSchema.getTownAccFeats(key);
	}

	public Map<String, Integer> getTownCountryInfo() {
		return townSchema.getTownCountryInfo();
	}

	public SM_Get_My_Fight_Info getMyFightInfo(Player player) {
		if (!townConfig.getTownModuleOpenCond().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		SM_Get_My_Fight_Info sm = new SM_Get_My_Fight_Info();
		sm.setInfos(townSchema.getMyFightInfo(player));
		return sm;
	}

	public SM_Get_Country_Fight_Info getCountryFightLog(Player player) {
		SM_Get_Country_Fight_Info sm = new SM_Get_Country_Fight_Info();
		sm.setInfos(townSchema.getCountryCaptureInfo(player));
		return sm;
	}

	public void applyCaptureTown(Player player, String key) {
		if (key == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		} else if (key.equals(player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey())) {
			return;
		}
		if (!townConfig.getTownModuleOpenCond().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		if (!townSchema.applyCaptureTown(player, key)) {
			throw new ManagedException(ManagedErrorCode.TOWN_APPLIED_BEEN_CAPTURE);
		}
	}

}
