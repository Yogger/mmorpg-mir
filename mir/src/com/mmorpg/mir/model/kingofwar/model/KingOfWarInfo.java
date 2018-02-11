package com.mmorpg.mir.model.kingofwar.model;

import java.util.Date;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.entity.KingOfWarEnt;
import com.mmorpg.mir.model.kingofwar.event.BecomeKingOfKingEvent;
import com.mmorpg.mir.model.kingofwar.packet.SM_KingOfWar_NotKingOfKing;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.DateUtils;

public class KingOfWarInfo {

	public static final StatEffectId KINGOFKING_STATE_ID = StatEffectId
			.valueOf("KINGOFKING", StatEffectType.KINGOFKING);

	private volatile long kingOfKing;

	private long becomeKingTime;
	/** 膜拜支持 */
	private volatile int supportCount;
	/** 膜拜鄙视 */
	private volatile int contemptCount;
	/** 皇城战开启的次数 */
	private int openCount;
	/** 皇帝每日礼包领取时间 */
	private long lastRewardTime;

	@Transient
	private EntityCacheService<Integer, KingOfWarEnt> kingOfWarEntEntDbService;

	@Transient
	private KingOfWarEnt ent;

	public static KingOfWarInfo valueOf() {
		KingOfWarInfo koi = new KingOfWarInfo();
		return koi;
	}

	public long getLastRewardTime() {
		return lastRewardTime;
	}

	public void setLastRewardTime(long lastRewardTime) {
		this.lastRewardTime = lastRewardTime;
	}

	@JsonIgnore
	public void setUpdate(EntityCacheService<Integer, KingOfWarEnt> kingOfWarEntEntDbService, KingOfWarEnt ent) {
		this.kingOfWarEntEntDbService = kingOfWarEntEntDbService;
		this.ent = ent;
	}

	/**
	 * 皇帝每日礼包
	 */
	@JsonIgnore
	public void rewardKingDaily() {
		if (kingOfKing != 0 && !DateUtils.isToday(new Date(lastRewardTime))) {
			Player kingPlayer = PlayerManager.getInstance().getPlayer(kingOfKing);
			Reward reward = RewardManager.getInstance().creatReward(kingPlayer,
					KingOfWarConfig.getInstance().KINGOFKING_DAY_REWARD.getValue(), null);
			I18nUtils titel18n = I18nUtils.valueOf(KingOfWarConfig.getInstance().KING_DAILY_REWARD_MAIL_TITLE
					.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(KingOfWarConfig.getInstance().KING_DAILY_REWARD_MAIL_CONTENT
					.getValue());
			contextl18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(kingPlayer.getName()));
			contextl18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(kingPlayer.getCountry().getName()));
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, kingPlayer.getObjectId());
			lastRewardTime = System.currentTimeMillis();
			this.update();
		}
	}

	@JsonIgnore
	public void becomeKing(Player player) {
		if (kingOfKing != 0) {
			Player oldKing = PlayerManager.getInstance().getPlayer(kingOfKing);
			oldKing.getGameStats().endModifiers(KingOfWarInfo.KINGOFKING_STATE_ID, true);
			PacketSendUtility.broadcastPacket(oldKing, SM_KingOfWar_NotKingOfKing.valueOf(kingOfKing), true);
		}
		kingOfKing = player.getObjectId();
		player.getGameStats().addModifiers(KingOfWarInfo.KINGOFKING_STATE_ID,
				KingOfWarConfig.getInstance().KINGOFKING_STATS.getValue(), true);
		// 皇帝的属性现在只通过坐骑来搞
		// if (ModuleOpenManager.getInstance().isOpenByModuleKey(player,
		// ModuleKey.HORSE)) {
		// player.getGameStats().replaceModifiers(Horse.GAME_STATE_ID,
		// player.getHorse().getStat(), true);
		// }
		// player.getHorse().getAppearance().sendNotFinishActive(player);
		becomeKingTime = System.currentTimeMillis();
		lastRewardTime = 0L;
		EventBusManager.getInstance().submit(BecomeKingOfKingEvent.valueOf(player.getObjectId()));
		update();
	}

	public void update() {
		kingOfWarEntEntDbService.writeBack(1, ent);
	}

	public long getKingOfKing() {
		return kingOfKing;
	}

	public void setKingOfKing(long kingOfKing) {
		this.kingOfKing = kingOfKing;
	}

	@JsonIgnore
	public EntityCacheService<Integer, KingOfWarEnt> getKingOfWarEntEntDbService() {
		return kingOfWarEntEntDbService;
	}

	public void setKingOfWarEntEntDbService(EntityCacheService<Integer, KingOfWarEnt> kingOfWarEntEntDbService) {
		this.kingOfWarEntEntDbService = kingOfWarEntEntDbService;
	}

	@JsonIgnore
	public KingOfWarEnt getEnt() {
		return ent;
	}

	@JsonIgnore
	public void setEnt(KingOfWarEnt ent) {
		this.ent = ent;
	}

	public long getBecomeKingTime() {
		return becomeKingTime;
	}

	public void setBecomeKingTime(long becomeKingTime) {
		this.becomeKingTime = becomeKingTime;
	}

	public int getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}

	public int getContemptCount() {
		return contemptCount;
	}

	public void setContemptCount(int contemptCount) {
		this.contemptCount = contemptCount;
	}

	@JsonIgnore
	public void refresh() {
		contemptCount = 0;
		supportCount = 0;
		update();
	}

	@JsonIgnore
	public void addCount() {
		if (ServerState.getInstance().isOpenServer()) {
			openCount++;
			update();
		}
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
}
