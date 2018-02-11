package com.mmorpg.mir.model.player.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mmorpg.mir.Debug;
import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.complexstate.ComplexStateType;
import com.mmorpg.mir.model.contact.manager.ContactManager;
import com.mmorpg.mir.model.controllers.effect.PlayerEffectController;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.service.DirtyWordsManager;
import com.mmorpg.mir.model.exchange.manager.ExchangeManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.operator.manager.OperatorManager;
import com.mmorpg.mir.model.player.entity.PlayerDetailVO;
import com.mmorpg.mir.model.player.entity.PlayerStat;
import com.mmorpg.mir.model.player.entity.PlayerVO;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.AccountStatus;
import com.mmorpg.mir.model.player.model.PlayerExpUpdate;
import com.mmorpg.mir.model.player.packet.SM_Another_Login;
import com.mmorpg.mir.model.player.packet.SM_Complex_Settings_Change;
import com.mmorpg.mir.model.player.packet.SM_Player_Detail;
import com.mmorpg.mir.model.player.resource.Role;
import com.mmorpg.mir.model.player.resource.SoulOfGeneralResource;
import com.mmorpg.mir.model.player.service.PlayerService;
import com.mmorpg.mir.model.rank.model.rank.WorldRank;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.ramcache.lock.ChainLock;
import com.windforce.common.ramcache.lock.LockUtils;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.socket.core.TSession;

@Component
public class PlayerServiceImpl implements PlayerService {

	private static final Logger logger = LoggerFactory.getLogger("STDOUT");

	@Autowired
	private PlayerManager playerManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private OperatorManager operatorManager;

	public int checkExsit(String account, String op, String server) {
		if (account == null) {
			return -1;
		}
		int result = 0;
		if (playerManager.isIndexExist(account, op, server)) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	public PlayerVO login(TSession session, String account, String op, String server, String sign, int isAdult,
			int loginType, int opVipLevel, String time) {
		if (!playerManager.isIndexExist(account, op, server)) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}
		if (!Debug.debug) {
			if (Long.valueOf(time) + DateUtils.MILLIS_PER_MINUTE * 30 < System.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.SIGN_DEPRECATED);
			}
		}

		if (!playerManager.validate(account, op, server, sign, time, opVipLevel)) {
			throw new ManagedException(ManagedErrorCode.VALIDATE_FAILURE);
		}

		Player player = playerManager.getPlayerByAccount(account, op, server);
		if (player == null) {
			logger.error(account + "_" + op + "_" + server + " 没有找到对应的账号或者玩家信息!");
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}
		if (operatorManager.isBan(player.getObjectId())) {
			throw new ManagedException(ManagedErrorCode.BAN_STATUS);
		}

		// 这里需要踢以前登陆的玩家下线
		TSession pre = sessionManager.getSession(player.getPlayerEnt().getId());
		if (pre != null) {
			SM_Another_Login sm = null;
			if (!pre.getInetIp().equals(session.getInetIp())) {
				sm = new SM_Another_Login();
			}
			pre.setAttribute("noEvent", true);
			if (sm == null) {
				sessionManager.kick(player.getPlayerEnt().getId());
			} else {
				sessionManager.kickAndSendClosePacket(sm, player.getPlayerEnt().getId());
			}
			PlayerManager.getInstance().logout(LogoutEvent.valueOf(player.getPlayerEnt().getId(), pre));
		}

		doLogin(session, player, isAdult, loginType, opVipLevel);

		// 这里需要返回
		PlayerVO vo = player.createPlayerVO();
		vo.setNeedInitSuperVip(operatorManager.isInitSuperVip(server));
		if (operatorManager.getSuperVip(server).isInit() && operatorManager.getSuperVip(server).isOpen()) {
			vo.setSuperVipVO(operatorManager.getSuperVip(server).createVO(
					player.getVip().isSuperVip(operatorManager.getSuperVip(server))));
			vo.getSuperVipVO().setTotalCharge(player.getVip().totalCharge(vo.getSuperVipVO().getCircleDay()));
		}
		return vo;
	}

	public PlayerVO createPlayer(TSession session, String account, String op, String server, String sign,
			String playerName, int role, int isAdult, int loginType, int country, String source, int noframe,
			String time, int opVip) {
		if (role == Role.SORCERER.value()) {
			throw new ManagedException(ManagedErrorCode.VALIDATE_FAILURE);
		}
		if (!Debug.debug) {
			if (Long.valueOf(time) + DateUtils.MILLIS_PER_MINUTE * 30 < System.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.SIGN_DEPRECATED);
			}
		}
		// 验证
		if (!playerManager.validate(account, op, server, sign, time, opVip)) {
			throw new ManagedException(ManagedErrorCode.VALIDATE_FAILURE);
		}
		if (StringUtils.containsWhitespace(playerName) || playerName.equals("s" + server + ".")
				|| playerName.contains(",") || playerName.contains(WorldRank.RANK_SEPERATOR)) {
			throw new ManagedException(ManagedErrorCode.NAME_CONTAINS_BLACK);
		}

		if (playerName.trim().getBytes().length > 40 || playerName.trim().getBytes().length < 2) {
			throw new ManagedException(ManagedErrorCode.OUT_OF_MAX_LENGTH);
		}

		// playerName命名是否合法
		if (DirtyWordsManager.getInstance().containsWords(playerName, WordsType.ROLEWORDS)) {
			throw new ManagedException(ManagedErrorCode.PLAYER_NAME_ILLEAGAL);
		}

		// 账户是否存在角色验证
		if (playerManager.isIndexExist(account, op, server)) {
			throw new ManagedException(ManagedErrorCode.HAVE_ROLE);
		}

		// 角色名是否重复验证
		if (playerManager.isNameExist(playerName)) {
			throw new ManagedException(ManagedErrorCode.NAME_REPEAT);
		}

		// 角色创建
		Player player = playerManager.createPlayer(account, playerName, role, server, op, country, source, noframe);

		LogManager.createLog(player, System.currentTimeMillis(), role, country, session.getInetIp());

		player.getPlayerStat().setCreateIp(session.getInetIp());

		doLogin(session, player, isAdult, loginType, opVip);

		player.getPlayerStat().setLoginIp(session.getInetIp());

		LogManager.loginLog(player, System.currentTimeMillis(), loginType, session.getInetIp(), player.getPlayerEnt()
				.getCountry());
		// 运营说想要1级的日志
		LogManager.levelUpLog(player, System.currentTimeMillis(), player.getSession().getInetIp());

		// 满血满蓝
		player.getLifeStats().balanceHpMp();
		PlayerVO vo = player.createPlayerVO();
		// 这里需要返回
		vo.setNeedInitSuperVip(operatorManager.isInitSuperVip(server));
		if (operatorManager.getSuperVip(server).isInit() && operatorManager.getSuperVip(server).isOpen()) {
			vo.setSuperVipVO(operatorManager.getSuperVip(server).createVO(
					player.getVip().isSuperVip(operatorManager.getSuperVip(server))));
			vo.getSuperVipVO().setTotalCharge(player.getVip().totalCharge(vo.getSuperVipVO().getCircleDay()));
		}
		return vo;
	}

	private void doLogin(TSession session, final Player player, int isAdult, int loginType, int opVip) {
		if (player.getPlayerEnt().getAdult() != isAdult) {
			player.getPlayerEnt().setAdult(isAdult);
		}

		// 设置玩家心跳时间
		player.setLastHeartBeatTime(System.currentTimeMillis());

		// 设置登陆身份信息
		session.setAttribute(SessionManager.IDENTITY, player.getPlayerEnt().getId());

		ChainLock lock = LockUtils.getLock(player);
		lock.lock();
		try {
			sessionManager.attach(session);
			player.setSession(session);
		} finally {
			lock.unlock();
		}

		// 从地图上移除
		if (player.getPosition() != null) {
			World.getInstance().despawn(player);
		}

		// 设置登陆状态
		player.getPlayerEnt().setStatus(AccountStatus.ONLINE.value());
		player.getPlayerEnt().setLoginType(loginType);

		PlayerStat stat = player.getPlayerEnt().getStat();

		// 设置登陆时间
		Date now = new Date();
		stat.setLastLogin(now);
		stat.setUnlineTime(stat.getUnlineTime() + (now.getTime() - stat.getLastLogoutTime().getTime()));
		player.getAddication().addUnlineTime(now.getTime() - stat.getLastLogoutTime().getTime());
		player.getAddication().startAddicationAntiFuture();

		// 设置连续登陆天数
		Date refreshToday = DateUtils.addTime(stat.getRefreshContinuousLoginTime(), 24, 0, 0);
		Date tomorrow = DateUtils.addTime(now, 24, 0, 0);
		if (DateUtils.isToday(refreshToday)) {
			stat.setContinuousLogin(stat.getContinuousLogin() + 1);
			stat.setRefreshContinuousLoginTime(now);
		} else if (!DateUtils.isSameDay(refreshToday, tomorrow)) {
			stat.setContinuousLogin(0);
			stat.setRefreshContinuousLoginTime(now);
		}
		if (!DateUtils.isToday(new Date(stat.getLastAccLoginNumberTime()))) {
			stat.setLastAccLoginNumberTime(System.currentTimeMillis());
			stat.setAccLoginNumber(stat.getAccLoginNumber() + 1);
		}
		// 设置回购背包
		player.setBuyBackPack(ItemStorage.valueOf(10, 10));

		// 这里需要重新计算一下背包的开启时间
		player.getPack().reset();
		player.getWareHouse().reset();
		player.getPack().refreshDeprecated();
		player.getWareHouse().refreshDeprecated();

		// 在系统模块初始完成以后，重新计算玩家的属性
		playerManager.resetPlayerGameStats(player);

		// 刷新重置
		player.getExpress().refresh();
		player.getRankInfo().refresh();
		player.getHorse().addObserver();
		player.getRp().addObserve();
		player.getHorse().refreshBlessing();
		player.getArtifact().refreshBlessing(player);
		player.getSoul().refreshBlessing(player);
		player.getPlayerCountryHistory().refresh();
		player.getWarship().refresh();
		player.getHorse().getAppearance().refreshDeprecated(player);
		player.getWelfare().getWelfareHistory().refresh();
		player.getWelfare().getOfflineExp().refresh(player);

		ModuleOpenManager.getInstance().refreshAll(player, true);

		ContactManager.getInstance().addObserver(player);
		// 添加业务触发器
		KingOfWarManager.getInstance().kingOfWarSpawnObsever(player);
		GangOfWarManager.getInstance().gangOfWarSpawnObsever(player);
		// 然后重启回血回蓝任务
		player.getLifeStats().updateCurrentStats();
		// 恢复BUFF
		((PlayerEffectController) player.getEffectController()).unSave();

		player.getPlayerStat().setLoginIp(session.getInetIp());
		player.getOperatorPool().getOperatorVip().setLevel(opVip);
		LogManager.loginLog(player, System.currentTimeMillis(), loginType, session.getInetIp(), player.getPlayerEnt()
				.getCountry());

		EventBusManager.getInstance().syncSubmit(LoginEvent.valueOf(player.getPlayerEnt().getId(), session, loginType));

		playerManager.updatePlayer(player);
	}

	public PlayerExpUpdate addExp(Player player, long exp, boolean log, ModuleInfo moduleInfo) {
		return playerManager.addExp(player, exp, log, moduleInfo);
	}

	public PlayerExpUpdate levelUpExp(Player player) {
		return playerManager.levelUp(player);
	}

	public void setKeyboard(Player player, String keys) {
		player.getPlayerEnt().setKeyboards(keys);
		playerManager.updatePlayer(player);
	}

	public void setClientSettings(Player player, String keys) {
		player.getPlayerEnt().setClientSettings(keys);
		playerManager.updatePlayer(player);
	}

	public void upgradeGeneralSpirit(Player player, int sign) {
		SoulOfGeneralResource resource = playerManager.soulOfGeneralResources.get(player.getPlayerEnt()
				.getSoulOfGeneral(), true);
		CoreConditions conditions = resource.getLightCondition();
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		int newSoul = player.getPlayerEnt().getSoulOfGeneral() + 1;
		SoulOfGeneralResource newResource = playerManager.soulOfGeneralResources.get(newSoul, true);
		player.getPlayerEnt().setSoulOfGeneral(newSoul);
		playerManager.updatePlayer(player);
		player.getGameStats().replaceModifiers(SoulOfGeneralResource.GENERAL_SPIRIT,
				newResource.getRoleStat(player.getRole()), true);
		PacketSendUtility.sendSignMessage(player, sign);
	}

	@Autowired
	private GangManager gangManager;

	@ReceiverAnno
	public void loginOut(LogoutEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (player != null) {
			try {
				logger.debug("playerId[{}] session[{}]登出事件！", new Object[] { player.getName(),
						player.getSession() == null ? null : player.getSession().getId() });
				player.getMoveController().stop();
				World.getInstance().despawn(player);
				((PlayerEffectController) player.getEffectController()).save();
				((PlayerEffectController) player.getEffectController()).logoutRemoveAllEffects();
				// 防沉迷
				player.getAddication().addOnlineTime();
				player.getAddication().stopAddicationsAntiFuture();
				// 取消交易
				if (player.isTrading())
					ExchangeManager.getInstance().cancelExchange(player, ManagedErrorCode.PLAYER_INLINE);
				ExchangeManager.getInstance().removeExchangeState(player);
				gangManager.refreshLastLoginOut(player);

				player.getPack().save();
				player.getWareHouse().save();
				player.getLifeStats().cancelAllTasks();
				player.getRequester().denyAll();
				player.setBuyBackPack(ItemStorage.valueOf(10, 10));
				player.getFootprintPool().stopFootprintFuture();

				// 删掉召唤物
				Summon summon = player.getSummon();
				if (summon != null) {
					summon.getAi().handleEvent(Event.DELETE);
				}

				// 清除观察者
				player.getObserveController().clear();
				player.breakGather();
				player.removeBackHome();
				player.removeTempleBrickCoolDown();

				player.getComplexState().bitSet2ByteArray();
				player.setDefaultMapChannel(0);
				player.getPlayerEnt().getStat().setLastLogoutTime(new Date());
				LogManager.loginOutLog(player, player.getPlayerEnt().getStat().getLastLogin().getTime(), player
						.getPlayerEnt().getStat().getLastLogoutTime().getTime(), player.getPlayerEnt().getLoginType(),
						player.getSession().getInetIp());
			} catch (Exception e) {
				logger.error("登出移除异常", e);
			}
		}
	}

	public void useItemAddRP(Player player, int addValue) {
		if (player.getRp() == null) {
			return;
		}
		player.getRp().increaseRP(addValue);
	}

	public void getPlayerInfo(Player player, long targetId) {
		if (targetId == player.getObjectId())
			return;
		PlayerDetailVO vo = playerManager.getPlayer(targetId).createPlayerDetailVO();
		Gang selfGang = player.getGang();
		PacketSendUtility
				.sendPacket(player, SM_Player_Detail.valueOf(selfGang == null ? null : selfGang.getName(), vo));
	}

	public void setComplexSettings(Player player, HashMap<Integer, Boolean> settings, int sign) {
		boolean broadcast = false;
		for (Entry<Integer, Boolean> entry : settings.entrySet()) {
			ComplexStateType type = ComplexStateType.valueOf(entry.getKey().intValue());
			boolean oldBoolean = player.getComplexState().isState(type);
			if (oldBoolean != entry.getValue().booleanValue()) {
				player.getComplexState().setState(true, entry.getValue().booleanValue(), type);
				broadcast = true;
			}
		}
		if (broadcast) { // complex settings change
			PacketSendUtility.broadcastPacketAndReceiver(player,
					SM_Complex_Settings_Change.valueOf(player.getObjectId(), player.getComplexState().getStates()));
		}
		PacketSendUtility.sendSignMessage(player, sign);
	}

}
