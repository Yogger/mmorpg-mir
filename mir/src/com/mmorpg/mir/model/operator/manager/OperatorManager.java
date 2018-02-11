package com.mmorpg.mir.model.operator.manager;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.operator.entity.OperatorEntity;
import com.mmorpg.mir.model.operator.model.ForbidChatList;
import com.mmorpg.mir.model.operator.model.GmList;
import com.mmorpg.mir.model.operator.model.GmPrivilege;
import com.mmorpg.mir.model.operator.model.GmPrivilegeType;
import com.mmorpg.mir.model.operator.model.LoginBanList;
import com.mmorpg.mir.model.operator.model.MobilePhone;
import com.mmorpg.mir.model.operator.model.OpForbidChat;
import com.mmorpg.mir.model.operator.model.OperatorVip;
import com.mmorpg.mir.model.operator.model.QiHu360PrivilegeLog;
import com.mmorpg.mir.model.operator.model.QiHu360PrivilegePlayer;
import com.mmorpg.mir.model.operator.model.QiHu360PrivilegeServer;
import com.mmorpg.mir.model.operator.model.QiHu360SpeedPrivilegePlayer;
import com.mmorpg.mir.model.operator.model.QiHu360SpeedPrivilegeServer;
import com.mmorpg.mir.model.operator.model.SubInformation;
import com.mmorpg.mir.model.operator.model.SuperVip;
import com.mmorpg.mir.model.operator.packet.SM_GM_Kick;
import com.mmorpg.mir.model.operator.packet.SM_Operator_Trace;
import com.mmorpg.mir.model.operator.packet.SM_QiHu360_Privilege_Change;
import com.mmorpg.mir.model.operator.packet.SM_QiHu360_Privilege_Reward;
import com.mmorpg.mir.model.operator.packet.SM_QiHu360_Speed_Privilege_Change;
import com.mmorpg.mir.model.operator.resource.Gift360Resource;
import com.mmorpg.mir.model.operator.resource.OperatorRewardResource;
import com.mmorpg.mir.model.operator.resource.OperatorVipResource;
import com.mmorpg.mir.model.operator.resource.OperatorVipResource2;
import com.mmorpg.mir.model.operator.resource.QiHu360PrivilegeResource;
import com.mmorpg.mir.model.operator.resource.UserReferRewardResource;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.AccountStatus;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class OperatorManager {

	/** 手机认证奖励id */
	@Static("OPERATOR:MOBILE_REWARD")
	private ConfigValue<String> MOBILE_REWARD;

	/** 完善个人信息奖励id */
	@Static("OPERATOR:SUBINFORMATION_REWARD")
	private ConfigValue<String> SUBINFORMATION_REWARD;

	/** 成为gm后的奖励 */
	@Static("OPERATOR:BECOME_GM_REWARD")
	private ConfigValue<String> BECOME_GM_REWARD;

	@Static
	private Storage<Integer, OperatorVipResource> operatorVipResources;

	@Static
	private Storage<Integer, OperatorVipResource2> operatorVipResources2;

	@Static
	private Storage<String, OperatorRewardResource> operatorRewardResources;

	@Static
	private Storage<String, UserReferRewardResource> userReferRewardResources;

	@Static
	private Storage<Integer, Gift360Resource> gift360Resources;

	@Static
	private Storage<String, QiHu360PrivilegeResource> qiHu360PrivilegeResource;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private CountryManager countryManager;

	@Autowired
	private ServerState serverState;

	private OperatorEntity operatorEntity;

	@Inject
	private EntityCacheService<Long, OperatorEntity> operatorEntityDB;

	/** 微信奖励id */
	@Static("OPERATOR:WECHAT_MAIL_REWARD")
	public ConfigValue<String> WECHAT_MAIL_REWARD;

	@Static("OPERATOR:QIHU_SPEED_BALL_MAIL_REWARD")
	public ConfigValue<String> QIHU_SPEED_BALL_MAIL_REWARD;

	@Static("PLATFORM_2345_REWARD")
	public ConfigValue<String> BROWSER_2345_REWARD;

	@Static("OPERATOR:PLATFORM_2345_CONDITION")
	public ConfigValue<String[]> BROWSER_2345_REWARD_CONDITION;

	@PostConstruct
	public void init() {
		operatorEntity = operatorEntityDB.loadOrCreate(1L, new EntityBuilder<Long, OperatorEntity>() {
			@Override
			public OperatorEntity newInstance(Long id) {
				return OperatorEntity.valueOf();
			}
		});
	}

	public void updateOperator() {
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
	}

	public boolean isForbidChat(long playerId) {
		return operatorEntity.getForbidChatList().isForbidChat(playerId);
	}

	public boolean isBan(long playerId) {
		return operatorEntity.getLoginBanList().isBan(playerId);
	}

	public boolean isInitSuperVip(String serverId) {
		return operatorEntity.getSuperVipPool().getSuperVip(serverId).isInit();
	}

	public SuperVip getSuperVip(String serverId) {
		return operatorEntity.getSuperVipPool().getSuperVip(serverId);
	}

	public OpForbidChat addChat(Player player, long endTime) {
		OpForbidChat ocf = operatorEntity.getForbidChatList().addForbidChat(player, endTime);
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
		playerManager.updatePlayer(player);
		return ocf;
	}

	public ForbidChatList getForbidChatList() {
		return operatorEntity.getForbidChatList();
	}

	public GmList getGmList() {
		return operatorEntity.getGmList();
	}

	public LoginBanList getLoginBanList() {
		return operatorEntity.getLoginBanList();
	}

	public void removeChat(long playerId) {
		operatorEntity.getForbidChatList().unForbidChat(playerId);
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
	}

	public void removeGm(long playerId) {
		operatorEntity.getGmList().removeGm(playerId);
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
	}

	public void addGm(Player player) {
		operatorEntity.getGmList().addGm(player);
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
	}

	public void addBanPlayer(Player player) {
		operatorEntity.getLoginBanList().addBan(player);
		player.getPlayerEnt().setStatus(AccountStatus.BAN.value());
		if (sessionManager.isOnline(player.getObjectId())) {
			sessionManager.kick(player.getObjectId());
		}
		playerManager.updatePlayer(player);
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
	}

	public void buildSuperVip(String name, String serverId, String contact, int minRecharge, int level, boolean open,
			String picturePath, int circleDay) {
		SuperVip superVip = operatorEntity.getSuperVipPool().getSuperVip(serverId);
		superVip.build(name, serverId, contact, minRecharge, level, open, picturePath, circleDay);
		operatorEntityDB.writeBack(operatorEntity.getId(), operatorEntity);
		// 通知
		if (superVip.isOpen()) {
			for (Country country : countryManager.getCountries().values()) {
				for (Player player : country.getCivils().values()) {
					if (player.getPlayerEnt().getServer().equals(serverId)) {
						PacketSendUtility.sendPacket(player, superVip.createVO(player.getVip().isSuperVip(superVip)));
					}
				}
			}
		}
	}

	public void unBanPlayer(Player player) {
		if (operatorEntity.getLoginBanList().unBan(player.getObjectId())) {
			player.getPlayerEnt().setStatus(AccountStatus.UNLINE.value());
			playerManager.updatePlayer(player);
		}
	}

	public void rewardMobileReward(Player player) {
		MobilePhone mobilePhone = player.getOperatorPool().getMobilePhone();
		if (mobilePhone.isRewarded()) {
			throw new ManagedException(ManagedErrorCode.MOBILEPHONE_REWARDED);
		}
		if (!mobilePhone.isConfirmation()) {
			throw new ManagedException(ManagedErrorCode.MOBILEPHONE_UNCONFIRMATION);
		}

		mobilePhone.setRewarded(true);
		rewardManager.grantReward(player, MOBILE_REWARD.getValue(),
				ModuleInfo.valueOf(ModuleType.OPERATOR_MOBILE, SubModuleType.MOBILE_REWARD));
	}

	public void rewardInformation(Player player) {
		SubInformation subInformation = player.getOperatorPool().getSubInformation();
		if (subInformation.isRewarded()) {
			return;
		}
		if (!subInformation.isConfirmation()) {
			return;
		}

		subInformation.setRewarded(true);
		rewardManager.grantReward(player, SUBINFORMATION_REWARD.getValue(),
				ModuleInfo.valueOf(ModuleType.OPERATOR_INFORMATION, SubModuleType.COMPLETE_INFO));
	}

	public void rewardOperatorVipReward(Player player, int level) {
		OperatorVip operatorVip = player.getOperatorPool().getOperatorVip();
		if (operatorVip.getRewarded().containsKey(level)) {
			if (operatorVip.getRewarded().get(level)) {
				throw new ManagedException(ManagedErrorCode.OPERATOR_REWARDED);
			}
		}
		if (operatorVip.getLevel() < level) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_LEVEL);
		}
		OperatorVipResource ovr = operatorVipResources.get(level, true);
		operatorVip.getRewarded().put(level, true);
		rewardManager.grantReward(player, ovr.getRewardId(),
				ModuleInfo.valueOf(ModuleType.OPERATOR_OPVIP, SubModuleType.OPERATOR_VIP_REWARD));
	}

	public void rewardOperatorVipReward2(Player player, Integer id) {
		OperatorVipResource2 vipResource2 = operatorVipResources2.get(id, true);
		OperatorVip operatorVip = player.getOperatorPool().getOperatorVip();
		boolean isContain = false;
		for (int i = 0; i < vipResource2.getVipLevel().length; i++) {
			if (vipResource2.getVipLevel()[i] == operatorVip.getLevel()) {
				isContain = true;
			}
		}
		if (!isContain) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_LEVEL);
		}

		if (OperatorVipResource2.RewardType.NICKNAME == vipResource2.getType()) {// nickname
			if (operatorVip.getNickNameRewarded().containsKey(id)) {
				throw new ManagedException(ManagedErrorCode.OPERATOR_REWARDED);
			} else {
				operatorVip.getNickNameRewarded().put(id, true);
			}
		} else {// level
			if (operatorVip.getLevelRewarded().containsKey(id)) {
				throw new ManagedException(ManagedErrorCode.OPERATOR_REWARDED);
			} else {
				// today has rewarded
				if (DateUtils.isSameDay(new Date(), new Date(operatorVip.getDailyRewardTime()))) {
					throw new ManagedException(ManagedErrorCode.OPERATOR_TODAY_REWARDED);
				}
				operatorVip.setDailyRewardTime(new Date().getTime());
				operatorVip.getLevelRewarded().put(id, true);
			}
		}
		rewardManager.grantReward(player, vipResource2.getRewardId(),
				ModuleInfo.valueOf(ModuleType.OPERATOR_OPVIP, SubModuleType.OPERATOR_VIP_REWARD));
	}

	public void ban(Player player, String targetName) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.BAN)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		PlayerEnt targetPlayerEnt = playerManager.getByName(targetName);
		if (targetPlayerEnt == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_PLAYER);
		}
		Player targetPlayer = playerManager.getPlayer(targetPlayerEnt.getGuid());
		if (targetPlayer.getOperatorPool().getGmPrivilege().isGm()) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GM);
		}
		addBanPlayer(targetPlayer);
	}

	public void unban(Player player, String targetName) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.BAN)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		PlayerEnt targetPlayerEnt = playerManager.getByName(targetName);
		if (targetPlayerEnt == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_PLAYER);
		}
		Player targetPlayer = playerManager.getPlayer(targetPlayerEnt.getGuid());
		if (targetPlayer.getOperatorPool().getGmPrivilege().isGm()) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GM);
		}
		unBanPlayer(targetPlayer);
	}

	public void showGMNickname(Player player) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.NICKNAME)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}

		player.getEffectController().setAbnormal(EffectId.GM_NICKNAME, true);
	}

	public void unShowGMNickname(Player player) {
		// GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		// if (!gmPrivilege.havePrivilege(GmPrivilegeType.NICKNAME)) {
		// throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		// }

		player.getEffectController().unsetAbnormal(EffectId.GM_NICKNAME, true);
	}

	public void hide(Player player) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.HIDE)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		player.getEffectController().setAbnormal(EffectId.GM_HIDE, true);
		player.getOperatorPool().getGmPrivilege().setOpenHide(true);
	}

	public void unHide(Player player) {
		// GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		// if (!gmPrivilege.havePrivilege(GmPrivilegeType.HIDE)) {
		// throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		// }
		player.getEffectController().unsetAbnormal(EffectId.GM_HIDE, true);
		player.getOperatorPool().getGmPrivilege().setOpenHide(false);
	}

	public void forbidChat(Player player, String targetName, int minuteTime) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.FORBID_CHAT)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		PlayerEnt targetPlayerEnt = playerManager.getByName(targetName);
		if (targetPlayerEnt == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_PLAYER);
		}
		Player targetPlayer = playerManager.getPlayer(targetPlayerEnt.getGuid());
		if (targetPlayer.getOperatorPool().getGmPrivilege().isGm()) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GM);
		}
		addChat(targetPlayer, System.currentTimeMillis() + DateUtils.MILLIS_PER_MINUTE * minuteTime);
	}

	public void removeChat(Player player, String targetName) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.FORBID_CHAT)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		PlayerEnt targetPlayerEnt = playerManager.getByName(targetName);
		if (targetPlayerEnt == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_PLAYER);
		}
		Player targetPlayer = playerManager.getPlayer(targetPlayerEnt.getGuid());
		if (targetPlayer.getOperatorPool().getGmPrivilege().isGm()) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GM);
		}
		removeChat(targetPlayer.getObjectId());
	}

	public void kick(Player player, String targetName) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.KICK)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		PlayerEnt targetPlayerEnt = playerManager.getByName(targetName);
		if (targetPlayerEnt == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_PLAYER);
		}
		Player targetPlayer = playerManager.getPlayer(targetPlayerEnt.getGuid());
		if (targetPlayer.getOperatorPool().getGmPrivilege().isGm()) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GM);
		}
		PacketSendUtility.sendPacket(targetPlayer, new SM_GM_Kick());
		sessionManager.kick(targetPlayer.getObjectId());
	}

	public SM_Operator_Trace trace(Player player, String targetName) {
		GmPrivilege gmPrivilege = player.getOperatorPool().getGmPrivilege();
		if (!gmPrivilege.havePrivilege(GmPrivilegeType.TRACE)) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GMPRIVILEGE);
		}
		PlayerEnt targetPlayerEnt = playerManager.getByName(targetName);
		if (targetPlayerEnt == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_PLAYER);
		}
		Player targetPlayer = playerManager.getPlayer(targetPlayerEnt.getGuid());
		if (targetPlayer.getOperatorPool().getGmPrivilege().isGm()) {
			throw new ManagedException(ManagedErrorCode.OPERATOR_GM);
		}
		if (!sessionManager.isOnline(targetPlayer.getObjectId())) {
			throw new ManagedException(ManagedErrorCode.PLAYER_INLINE);
		}
		return SM_Operator_Trace.valueOf(targetPlayer, 0);
	}

	public void setGmPrivilege(Player player, HashSet<Integer> privileges) {
		if (privileges == null || privileges.isEmpty()) {
			player.getOperatorPool().getGmPrivilege().setGm(false);
			removeGm(player.getObjectId());
		} else {
			player.getOperatorPool().getGmPrivilege().setGm(true);
			addGm(player);
			rewardManager.grantReward(player, BECOME_GM_REWARD.getValue(),
					ModuleInfo.valueOf(ModuleType.OPERATOR_GM, SubModuleType.OPERATOR_GM_REARD));
		}
		player.getOperatorPool().getGmPrivilege().setPrivileges(privileges);
		if (player.getOperatorPool().getGmPrivilege().havePrivilege(GmPrivilegeType.HIDE)) {
			hide(player);
		} else {
			unHide(player);
		}
		if (player.getOperatorPool().getGmPrivilege().havePrivilege(GmPrivilegeType.NICKNAME)) {
			player.getEffectController().setAbnormal(EffectId.GM_NICKNAME, true);
		} else {
			player.getEffectController().unsetAbnormal(EffectId.GM_NICKNAME, true);
		}
		playerManager.updatePlayer(player);
		PacketSendUtility.sendPacket(player, player.getOperatorPool().getGmPrivilege());
	}

	public void reward360gift(Player player) {
		OperatorVip ov = player.getOperatorPool().getOperatorVip();
		if (DateUtils.isToday(new Date(ov.getLastReward360giftTime()))) {
			throw new ManagedException(ManagedErrorCode.GIFT360_TIME);
		}
		Gift360Resource gift360Resource = gift360Resources.get(ov.getCount360gift() + 1, false);
		if (gift360Resource == null) {
			throw new ManagedException(ManagedErrorCode.GIFT360_COUNT);
		}

		ov.addCount360gift();
		rewardManager.grantReward(player, gift360Resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.GIFT360, SubModuleType.GIFT360));
	}

	public void qihuSpeedBallReward(Player player) {
		if (player.getOperatorPool().isQihuSpeedBallGiftRewarded()) {
			throw new ManagedException(ManagedErrorCode.QIHU_SPEED_BALL_REWARD);
		}
		rewardManager.grantReward(player, QIHU_SPEED_BALL_MAIL_REWARD.getValue(),
				ModuleInfo.valueOf(ModuleType.GIFT360, SubModuleType.GIFT360));
		player.getOperatorPool().setQihuSpeedBallGiftRewarded(true);
		playerManager.updatePlayer(player);
	}

	public void wechatReward(Player player) {
		if (player.getOperatorPool().isWechatGiftRewarded()) {
			throw new ManagedException(ManagedErrorCode.WECHAT_REWARDED);
		}
		rewardManager.grantReward(player, WECHAT_MAIL_REWARD.getValue(),
				ModuleInfo.valueOf(ModuleType.OPERATOR_WECHAT, SubModuleType.WECHAT));
		player.getOperatorPool().setWechatGiftRewarded(true);
		playerManager.updatePlayer(player);
	}

	public void browser2345Reward(Player player) {
		if (player.getOperatorPool().isBrowser2345Rewarded()) {
			throw new ManagedException(ManagedErrorCode.BROWSER_2345_REWARDED);
		}
		if (!CoreConditionManager.getInstance().getCoreConditions(1, BROWSER_2345_REWARD_CONDITION.getValue())
				.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		rewardManager.grantReward(player, BROWSER_2345_REWARD.getValue(),
				ModuleInfo.valueOf(ModuleType.OPERATOR_WECHAT, SubModuleType.BROWSER_2345_REWARD));
		player.getOperatorPool().setBrowser2345Rewarded(true);
	}

	public void createReward(Player player) {
		String op = player.getPlayerEnt().getOp();
		OperatorRewardResource operatorReward = operatorRewardResources.get(op, false);
		if (operatorReward != null && !StringUtils.isNullOrEmpty(operatorReward.getRewardId())) {
			rewardManager.grantReward(player, operatorReward.getRewardId(),
					ModuleInfo.valueOf(ModuleType.OPERATOR_REWARD, SubModuleType.OPERATOR_REWARD));
		}

		String refer = player.getPlayerEnt().getUserRefer();
		UserReferRewardResource userReferRewardResource = userReferRewardResources.get(refer, false);
		if (userReferRewardResource != null && !StringUtils.isNullOrEmpty(userReferRewardResource.getRewardId())) {
			if (userReferRewardResource.getMailTitle() == null) {
				rewardManager.grantReward(player, userReferRewardResource.getRewardId(),
						ModuleInfo.valueOf(ModuleType.OPERATOR_REWARD, SubModuleType.REFER_REWARD));
			} else {
				Reward reward = rewardManager.creatReward(player, Arrays.asList(userReferRewardResource.getRewardId()));
				Mail mail = Mail.valueOf(I18nUtils.valueOf(userReferRewardResource.getMailTitle()),
						I18nUtils.valueOf(userReferRewardResource.getMailContent()), null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}
		}

	}

	public void notifyAllPalyerQiHu360PrivilegeServerChanged() {
		QiHu360PrivilegeServer qiHuServer = serverState.getQiHu360PrivilegeServer();
		SM_QiHu360_Privilege_Change sm = SM_QiHu360_Privilege_Change.valueOf(qiHuServer);
		for (Country country : countryManager.getCountries().values()) {
			for (Player player : country.getCivils().values()) {
				if (qiHuServer.getVersion() != player.getOperatorPool().getQiHuPrivilege().getVersion()) {
					player.getOperatorPool().getQiHuPrivilege().reset(qiHuServer);
				}
				PacketSendUtility.sendPacket(player, sm);
			}
		}
	}

	public SM_QiHu360_Privilege_Reward rewardQiHu360Privilege(Player player, String rewardId) {
		QiHu360PrivilegeResource qpResource = qiHu360PrivilegeResource.get(rewardId, true);
		QiHu360PrivilegeServer qihuServer = serverState.getQiHu360PrivilegeServer();
		QiHu360PrivilegePlayer qihuPlayer = player.getOperatorPool().getQiHuPrivilege();
		if (qihuServer.getVersion() != qihuPlayer.getVersion()) {
			qihuPlayer.reset(qihuServer);
		}
		// 判断用户是否已经领取
		if (!qpResource.isEveryDay()) {
			if (qihuPlayer.hasDrawBefore(rewardId)) {
				throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_HAS_DRAW);
			}
		} else if (qihuPlayer.isTodayRewarded(rewardId)) {
			throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_HAS_DRAW);
		}

		// 条件验证
		if (!qpResource.getCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		// 发放奖励
		RewardManager.getInstance().grantReward(player, Arrays.asList(qpResource.getRewardIds()),
				ModuleInfo.valueOf(ModuleType.OPERATOR_REWARD, SubModuleType.OPERATOR_REWARD));
		qihuPlayer.addQiHu360PrivilegeLog(QiHu360PrivilegeLog.valueOf(rewardId));
		return SM_QiHu360_Privilege_Reward.valueOf(0, rewardId);
	}

	public void notifyAllPalyerQiHu360SpeedPrivilegeServerChanged() {
		QiHu360SpeedPrivilegeServer qiHuServer = serverState.getQiHu360SpeedPrivilegeServer();
		SM_QiHu360_Speed_Privilege_Change sm = SM_QiHu360_Speed_Privilege_Change.valueOf(qiHuServer);
		for (Country country : countryManager.getCountries().values()) {
			for (Player player : country.getCivils().values()) {
				if (qiHuServer.getVersion() != player.getOperatorPool().getQiHuSpeedPrivilege().getVersion()) {
					player.getOperatorPool().getQiHuSpeedPrivilege().reset(qiHuServer);
				}
				PacketSendUtility.sendPacket(player, sm);
			}
		}
	}

	public void rewardQiHu360SpeedPrivilege(Player player, String rewardId) {
		QiHu360PrivilegeResource qpResource = qiHu360PrivilegeResource.get(rewardId, true);
		QiHu360SpeedPrivilegeServer qihuSpeedServer = serverState.getQiHu360SpeedPrivilegeServer();
		QiHu360SpeedPrivilegePlayer qihuSpeedPlayer = player.getOperatorPool().getQiHuSpeedPrivilege();
		// 条件验证
		if (!qpResource.getCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		// 判断用户领取是否在活动时间
		if (!qihuSpeedServer.isInPrivilegeTime()) {
			throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_NO_CONDITION);
		}
		// 判断用户当天是否已经领取是否已经领取
		if (qihuSpeedPlayer.hasDrawToDay()) {
			throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_HAS_DRAW);
		}
		// 发放奖励
		RewardManager.getInstance().grantReward(player, Arrays.asList(qpResource.getRewardIds()),
				ModuleInfo.valueOf(ModuleType.OPERATOR_REWARD, SubModuleType.OPERATOR_REWARD));
		qihuSpeedPlayer.addDrawLog(new Date().getTime());
	}
}
