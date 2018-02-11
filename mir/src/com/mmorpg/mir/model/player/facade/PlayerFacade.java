package com.mmorpg.mir.model.player.facade;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.Debug;
import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ServerConfigValue;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.commonactivity.manager.CommonActivityManager;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.cooldown.PublicCooldown;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.RequestHandlerType;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.OntheHourEvent;
import com.mmorpg.mir.model.player.event.PlayerDieEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.manager.RPManager;
import com.mmorpg.mir.model.player.model.PlayerExpUpdate;
import com.mmorpg.mir.model.player.packet.CM_CheckExist;
import com.mmorpg.mir.model.player.packet.CM_CheckNameExist;
import com.mmorpg.mir.model.player.packet.CM_ComplexStat_Settings;
import com.mmorpg.mir.model.player.packet.CM_CreatePlayer;
import com.mmorpg.mir.model.player.packet.CM_Heartbeat;
import com.mmorpg.mir.model.player.packet.CM_Login;
import com.mmorpg.mir.model.player.packet.CM_Query_Player;
import com.mmorpg.mir.model.player.packet.CM_Question_Response;
import com.mmorpg.mir.model.player.packet.CM_SetAutoBattle;
import com.mmorpg.mir.model.player.packet.CM_Upgrade_General_Soul;
import com.mmorpg.mir.model.player.packet.SM_CheckExist;
import com.mmorpg.mir.model.player.packet.SM_CheckNameExist;
import com.mmorpg.mir.model.player.packet.SM_CreatePlayer;
import com.mmorpg.mir.model.player.packet.SM_Heartbeat;
import com.mmorpg.mir.model.player.packet.SM_Login;
import com.mmorpg.mir.model.player.packet.SM_PlayerExpUpdate;
import com.mmorpg.mir.model.player.service.PlayerService;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.effecttemplate.GrayEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.suicide.event.SuicideTurnEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.vip.event.VipEvent;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.utils.CharCheckUtil;
import com.mmorpg.mir.utils.HttpUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.scheduler.Scheduled;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.collection.ConcurrentHashSet;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class PlayerFacade {
	private static final Logger logger = Logger.getLogger(PlayerFacade.class);
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerManager playerManager;
	@Autowired
	private ServerConfigValue serverConfigValue;
	// @Autowired
	// private PlayerChatTransportManager playerChatTransportManager;

	@Autowired
	private EventBusManager eventBusManager;

	private ConcurrentHashMap<String, Object> accountLockMap = new ConcurrentHashMap<String, Object>();

	private ConcurrentHashMap<String, Object> nameLockMap = new ConcurrentHashMap<String, Object>();

	private ConcurrentHashSet<String> notForbidSelectCountryPlayerAccount = new ConcurrentHashSet<String>();

	public static volatile boolean masterOpen = false;

	@HandlerAnno
	public SM_CheckExist checkExsit(TSession session, CM_CheckExist req) {
		SM_CheckExist sm = new SM_CheckExist();
		String account = req.getAccount();
		try {
			Object lock = getAccountLock(account);
			int exist = 0;
			synchronized (lock) {
				exist = playerService.checkExsit(account, req.getOp(), req.getServer());
			}
			sm.setResult(exist);
			if (exist == 0) {
				if ((serverConfigValue.getMasterAccountUrl() == null && !Debug.debug) || !masterOpen) {
					notForbidSelectCountryPlayerAccount.add(req.getAccount());
				} else {
					String url = serverConfigValue.getMasterAccountUrl()
							+ String.format("?account=%s&serverId=%s&platCode=%s", req.getAccount(), req.getServer(),
									req.getOp());
					if (Debug.debug || HttpUtil.isMasterAccount(url)) {
						int forbidCountry = playerManager.forbidSelectCountry();
						if (forbidCountry == 0) {
							notForbidSelectCountryPlayerAccount.add(req.getAccount());
						}
						sm.setForbidCountry((byte) forbidCountry);
					} else {
						notForbidSelectCountryPlayerAccount.add(req.getAccount());
					}
				}
				sm.setCountry((byte) playerManager.selectCountry());
				if (sm.getCountry() == sm.getForbidCountry()) {
					int selectCountry = sm.getForbidCountry() - 1;
					sm.setCountry(selectCountry < 1 ? (byte) (sm.getForbidCountry() + 1) : (byte) selectCountry);
				}
				sm.setRole((byte) playerManager.selectRole());

				LogManager.checkLog(account, System.currentTimeMillis(), session.getInetIp(), req.getSource());
			} else {
				playerManager.transferAddress(req.getAccount(), req.getOp(), req.getServer(), sm);
			}
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家检查角色存在处理异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		} finally {
			accountLockMap.remove(account);
		}
		return sm;
	}

	@HandlerAnno
	public SM_CheckNameExist checkNameExsit(TSession session, CM_CheckNameExist req) {
		byte result = (playerManager.isNameExist(req.getName()) ? (byte) 1 : 0);
		return SM_CheckNameExist.valueOf(result);
	}

	@HandlerAnno
	public SM_Login login(TSession session, CM_Login req) {
		SM_Login sm = new SM_Login();
		String account = req.getAccountName();
		try {
			Object lock = getAccountLock(account);
			synchronized (lock) {
				sm.setPlayerVO(playerService.login(session, req.getAccountName(), req.getOp(), req.getServer(),
						req.getSign(), req.getIsAdult(), req.getLoginType(), req.getOpVipLevel(), req.getTime()));
			}
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家登陆处理异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		} finally {
			accountLockMap.remove(account);
		}
		return sm;
	}

	@HandlerAnno
	public SM_CreatePlayer createPlayer(TSession session, CM_CreatePlayer req) {
		SM_CreatePlayer sm = new SM_CreatePlayer();
		// 这里直接先判断下名字
		if (!CharCheckUtil.checkString(req.getPlayerName())) {
			sm.setCode(ManagedErrorCode.PLAYER_NAME_ILLEAGAL);
			return sm;
		}

		String account = req.getAccount();

		String name = "";
		if (StringUtils.isBlank(ServerState.getInstance().getServerName())) {
			name = "s" + req.getServer() + "." + req.getPlayerName();
		} else {
			name = ServerState.getInstance().getServerName() + "." + req.getPlayerName();
		}
		req.setPlayerName(name);
		try {
			Object alock = getAccountLock(account);
			Object nlock = getNameLock(name);
			synchronized (alock) {
				synchronized (nlock) {
					// boolean notForbidSelectCountry =
					// (notForbidSelectCountryPlayerAccount.contains(req.getAccount())
					// || false);
					boolean forbidSelectCountry = false;
					sm.setPlayerVO(playerService.createPlayer(session, req.getAccount(), req.getOp(), req.getServer(),
							req.getSign(), req.getPlayerName(), req.getRole(), req.getIsAdult(), req.getLoginType(),
							req.getCountry(), req.getSource(), req.getNoframe(), req.getTime(), req.getOpVipLevel(),
							forbidSelectCountry, req.getUserRefer()));
					notForbidSelectCountryPlayerAccount.remove(req.getAccount());
				}
			}
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家创建角色处理异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		} finally {
			accountLockMap.remove(account);
			nameLockMap.remove(name);
		}
		return sm;
	}

	@HandlerAnno
	public void questionResponse(TSession session, CM_Question_Response req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			player.getRequester().respond(RequestHandlerType.valueOf(req.getQuestionid()), req.getResponse());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家登陆处理异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	private static final int HEART_BEAT_GATE = 3000;

	@HandlerAnno
	public SM_Heartbeat heartbeat(TSession session, CM_Heartbeat req) {
		SM_Heartbeat sm = new SM_Heartbeat();
		sm.setTime(System.currentTimeMillis());
		// 心跳处理
		Player player = SessionUtil.getPlayerBySession(session);
		if (player != null) {
			if (req.getClientTime() > (System.currentTimeMillis() + HEART_BEAT_GATE)) {

				// 记录加速日志
				LogManager.heartBeatLog(player, session.getInetIp(), req.getClientTime(), sm.getTime());

				// 加速外挂，干掉
				if (player.increaseWarn()) {
					session.close();
				}
			}

			if (req.getClientTime() <= 0) {

				// 记录加速日志
				LogManager.heartBeatLog(player, session.getInetIp(), req.getClientTime(), sm.getTime());

				// 加速外挂，干掉
				if (player.increaseWarn()) {
					// 外挂
					session.close();
				}
			}

			if (req.getClientTime() < player.getLastHeartBeatTime()) {

				// 记录加速日志
				LogManager.heartBeatLog(player, session.getInetIp(), req.getClientTime(), sm.getTime());

				// 加速外挂，干掉
				if (player.increaseWarn()) {
					// 外挂
					session.close();
				}
			}

			// mailManager.receiveGroupMail(player.getObjectId());
			// EventBusManager.getInstance().submit(HeartbeatEvent.valueOf(player.getObjectId()));
			player.getPack().refreshDeprecated();
			player.getWareHouse().refreshDeprecated();
			player.getHorse().getAppearance().refreshDeprecated(player);
			player.getArtifact().checkBuffDeprect();
			player.getNicknamePool().checkDeprecat(true);
			player.getCountry().getNewTechnology().refreshFlagCount();
			int heartBeatTime = player.getHeartBeatTime();
			if (++heartBeatTime % 10 == 0) {
				PlayerManager.getInstance().updatePlayer(player);
			}
			player.setHeartBeatTime(heartBeatTime);
			player.setLastHeartBeatTime(sm.getTime());
		}
		// player.getRp().refresh();
		return sm;
	}

	@Autowired
	private SessionManager sessionManager;

	@HandlerAnno
	public void setAutoBattle(TSession session, CM_SetAutoBattle req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (req.getAutoBattle() != null && req.getAutoBattle().length > 50) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			player.getPlayerEnt().setAutoBattle(req.getAutoBattle());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家自动挂机出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}

	}

	@HandlerAnno
	public void getPlayerInfo(TSession session, CM_Query_Player req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			playerService.getPlayerInfo(player, req.getTargetId());
		} catch (Exception e) {
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@Scheduled(name = "系统0点所有在线玩家抛出事件", value = "0 0 0 * * *")
	public void anotherDay() {
		// 刷新国家运镖搬砖
		CountryManager.getInstance().refreshCountryQuest();
		// 计算国家战斗力
		WorldRankManager.getInstance().calculateCountryLevel(false);
		// 公测团购邮件奖励
		OpenActiveManager.getInstance().rewardGroupPurchaseMail();
		OpenActiveManager.getInstance().rewardGroupPurchaseTwoMail();
		OpenActiveManager.getInstance().rewardGroupPurchaseThreeMail();

		OpenActiveManager.getInstance().rewardCelebrateRecharge();
		// 发放皇帝每日礼包
		KingOfWarManager.getInstance().rewardKingDaily();
		for (long playerId : sessionManager.getOnlineIdentities()) {
			eventBusManager.submit(AnotherDayEvent.valueOf(playerId));
		}
		// 增加暴击活动开启次数
		ServerState.getInstance().checkAndAddWeekCriCount();

		// 消费活动补偿
		CommonActivityManager.getInstance().mailCompensateAll();
	}

	/**
	 * 当前只有9点整
	 */
	@Scheduled(name = "整点时所有在线玩家抛出事件", value = "0 0 9 * * *")
	public void onTheHour() {
		for (long playerId : sessionManager.getOnlineIdentities()) {
			eventBusManager.submit(OntheHourEvent.valueOf(playerId));
		}
	}

	/**
	 * 当前只有1点整
	 */
	// @Scheduled(name = "地图封边检测", value = "0 0 1 * * *")
	public void forbidMap() {
		for (long playerId : sessionManager.getOnlineIdentities()) {
			Player player = playerManager.getPlayer(playerId);
			if (player.getPosition() != null && !player.isInCopy()) {
				MapResource mapResource = World.getInstance().getMapResource(player.getMapId());
				if (mapResource.getCountry() != player.getCountryValue()) {
					if (mapResource.getForbidEnterCondition() != null) {
						if (mapResource.getForbidEnterCondition().verify(player, false)) {
							List<String> result = ChooserManager.getInstance().chooseValueByRequire(
									player.getCountryValue(), PlayerManager.getInstance().BACKHOME_POINT.getValue());
							RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
							World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), (byte) 0);
							player.sendUpdatePosition();
							PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MAP_FORBIDTIME1);
						}
					}
				}
			}
		}
	}

	/**
	 * 封边前一分钟通报
	 */
	// @Scheduled(name = "地图封边检测通报", value = "0 59 0 * * *")
	public void forbidMapNotice() {
		I18nUtils i18nUtils1 = I18nUtils.valueOf("402001");
		ChatManager.getInstance().sendSystem(71001, i18nUtils1, null);
	}

	@ReceiverAnno
	public void logGrayAndDeadNotice(PlayerDieEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		Player attacker = playerManager.getPlayer(event.getAttackerId());
		player.getRp().doGray(); // 记录灰名
		if (event.isKilledByPlayer()) {
			CoreConditions conditions = PlayerManager.getInstance().getDeadNoticeConditions();
			int mapCountry = World.getInstance().getWorldMap(player.getMapId()).getCountry().getValue();
			if (conditions.verify(player, false)) {
				if (mapCountry == player.getCountryValue()) {
					// I18nUtils utils = I18nUtils
					// .valueOf("306012")
					// .addParm("name", I18nPack.valueOf(player.getName()))
					// .addParm("map",
					// I18nPack.valueOf(World.getInstance().getMapResource(player.getMapId()).getName()))
					// .addParm("targetname",
					// I18nPack.valueOf(attacker.getName()))
					// .addParm("targetCountry",
					// I18nPack.valueOf(attacker.getCountry().getName()));
					// ChatManager.getInstance().sendSystem(6, utils, null,
					// player.getCountry());
				}
				if (player.isKing()) {
					I18nUtils i18nUtils = I18nUtils.valueOf("10308");
					i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
					i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
					i18nUtils.addParm("targetCountry", I18nPack.valueOf(attacker.getCountry().getName()));
					i18nUtils.addParm("targetName", I18nPack.valueOf(attacker.getName()));
					ChatManager.getInstance().sendSystem(11001, i18nUtils, null);
				}
				int group = PlayerManager.getInstance().DEAD_NOTICE_CD_GROUP.getValue();
				player.setPublicCoolDown(group, System.currentTimeMillis()
						+ PublicCooldown.getInstance().getPublicCooldown(group));
			}
		}
		// RPService.getInstance().killAddRp(attacker, player);
	}

	@HandlerAnno
	public void setComplexSettings(TSession session, CM_ComplexStat_Settings req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			playerService.setComplexSettings(player, req.getComplexSettings(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家登陆处理异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public void upgradeGeneralSoul(TSession session, CM_Upgrade_General_Soul req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			playerService.upgradeGeneralSpirit(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家登陆处理异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@ReceiverAnno
	public void anotherDayRefresh(AnotherDayEvent anotherDayEvent) {
		Player player = playerManager.getPlayer(anotherDayEvent.getOwner());
		player.getExpress().refresh();
		PacketSendUtility.sendPacket(player, player.getExpress().createVO());
	}

	@ReceiverAnno
	public void vipEventDPMax(VipEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getLifeStats().restoreDp();
	}

	@ReceiverAnno
	public void freshManLogin(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getLevel() < PlayerManager.getInstance().FRESHMAN_PROTECT.getValue()) {
			player.getEffectController().setAbnormal(EffectId.FRESHMAN.getEffectId(), true);
		}
	}

	@ReceiverAnno
	public void checkFreshManStatus(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getEffectController().isAbnoramlSet(EffectId.FRESHMAN)
				&& player.getLevel() >= PlayerManager.getInstance().FRESHMAN_PROTECT.getValue()) {
			player.getEffectController().unsetAbnormal(EffectId.FRESHMAN.getEffectId(), true);
		}
	}

	@ReceiverAnno
	public void weakenGrayBuff(KillPlayerEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getEffectController().contains(GrayEffect.GRAY)) {
			player.getRp().weakenGrayBuff(true);
			if (player.getRp().getGrayLevel() == 0) {
				player.getEffectController().removeEffect(GrayEffect.GRAY);
			} else {
				long duration = player.getEffectController().getAnormalEffect(GrayEffect.GRAY).getEndTime()
						- System.currentTimeMillis();
				Skill skill = SkillEngine.getInstance().getSkill(null,
						RPManager.getInstance().getGraySkillId(player.getRp().getGrayLevel()), player.getObjectId(), 0,
						0, player, null);
				skill.noEffectorUseSkill(duration);
			}
		}
	}

	@ReceiverAnno
	public void weakenGrayBuffByKillNPC(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getEffectController().contains(GrayEffect.GRAY) && player.getRp().getGrayLevel() > 0) {
			player.getRp().weakenGrayBuff(false);
			long duration = player.getEffectController().getAnormalEffect(GrayEffect.GRAY).getEndTime()
					- System.currentTimeMillis();
			Skill skill = SkillEngine.getInstance().getSkill(null,
					RPManager.getInstance().getGraySkillId(player.getRp().getGrayLevel()), player.getObjectId(), 0, 0,
					player, null);
			skill.noEffectorUseSkill(duration);
		}
	}

	@ReceiverAnno
	public void suicideTurn(SuicideTurnEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		PlayerExpUpdate update = playerService.suicideTurn(player);
		if (update == null) {
			return;
		}
		PacketSendUtility.sendPacket(player,
				SM_PlayerExpUpdate.valueOf(update.getLevel(), update.getAddExp(), update.getExp()));

	}

	private Object getAccountLock(String key) {
		return getLock(accountLockMap, key);
	}

	private Object getNameLock(String key) {
		return getLock(nameLockMap, key);
	}

	private Object getLock(ConcurrentMap<String, Object> lockMap, String key) {
		Object lock = lockMap.get(key);
		if (lock == null) {
			Object temp = new Object();
			lock = lockMap.putIfAbsent(key, temp);
			if (lock == null) {
				lock = temp;
			}
		}
		return lock;
	}
}
