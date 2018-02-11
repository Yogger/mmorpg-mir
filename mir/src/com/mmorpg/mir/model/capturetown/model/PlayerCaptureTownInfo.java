package com.mmorpg.mir.model.capturetown.model;

import java.util.Date;
import java.util.concurrent.Future;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.capturetown.TownCaptureEvent;
import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.capturetown.packet.SM_Reward_Base_Feats;
import com.mmorpg.mir.model.capturetown.resource.TownResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.shop.packet.vo.ShoppingHistoryVO;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class PlayerCaptureTownInfo {

	/** 每日挑战城池的次数和领奖的次数 **/
	private int dailyCount;

	/** 每日挑战城池的次数 **/
	private int dailyBuyCount;

	/** 最后一次重置的时间 **/
	private transient long lastResetTime;

	/** 清除CD的次数 **/
	private int clearCDCount;

	/** 累计的CD时间 **/
	private long nextChallengeTime;

	/** 最后一次进入挑战的时间 **/
	private long lastEnterTime;
	
	/** 个人俸禄 **/
	private transient long baseRewardTime;
	
	/** 抢占的城池Key **/
	private volatile String catpureTownKey; 
	
	/** 今天总共的个人俸禄 **/
	private long todayTotalReward;
	
	/** 今天总共的个人俸禄的刷新时间标志 **/
	private transient long lastDailyResetTime;
	
	@Transient
	private transient Future<?> autoRewardSelfFuture;
	
	@Transient
	private transient Player owner;
	
	@JsonIgnore
	public void startAutoRewardTask(boolean login) {
		if (!TownConfig.getInstance().getTownModuleOpenCond().verify(owner, false)) {
			return;
		}
		if (login) {
			offlineAutoReward(owner.getObjectId());
			refresh();
			refreshDaily();
		}
		if (catpureTownKey == null) {
			catpureTownKey = TownConfig.getInstance().getSpecialTown().getId();
		}
		if (autoRewardSelfFuture == null || autoRewardSelfFuture.isCancelled()) {
			autoRewardSelfFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					TownResource resource = TownConfig.getInstance().townResources.get(catpureTownKey, false);
					if (resource == null) {
						return;
					}
					int addValue = resource.getBaseFeatsPerMin() * resource.getBaseFeatsCalcRate();
					Reward reward = Reward.valueOf().addCurrency(CurrencyType.FEATS, addValue);
					RewardManager.getInstance().grantReward(owner, reward, 
							ModuleInfo.valueOf(ModuleType.CAPTURE_TOWN, SubModuleType.BASE_FEATS_AUTO_REWARD));
					baseRewardTime = System.currentTimeMillis();
					todayTotalReward += addValue;
					PacketSendUtility.sendPacket(owner, SM_Reward_Base_Feats.valueOf(reward));
				}
			}, TownConfig.getInstance().getTownResource(catpureTownKey).getBaseFeatsCalcRate() * DateUtils.MILLIS_PER_MINUTE, 
				TownConfig.getInstance().getTownResource(catpureTownKey).getBaseFeatsCalcRate() * DateUtils.MILLIS_PER_MINUTE);
		}
	}
	
	@JsonIgnore
	public void offlineAutoReward(Long playerId) {
		if (catpureTownKey == null || baseRewardTime == 0L) {
			return;
		}
		long interval = System.currentTimeMillis() - baseRewardTime;
		if (interval > (TownConfig.getInstance().COUNTRY_CAPTURE_OFFLINE_LIMIT.getValue() * DateUtils.MILLIS_PER_MINUTE)) {
			interval = TownConfig.getInstance().COUNTRY_CAPTURE_OFFLINE_LIMIT.getValue() * DateUtils.MILLIS_PER_MINUTE;
		}
		TownResource resource = TownConfig.getInstance().getTownResource(catpureTownKey);
		int count = (int) (interval / (resource.getBaseFeatsCalcRate() * DateUtils.MILLIS_PER_MINUTE));
		
		if (count > 0) {
			Reward reward = Reward.valueOf().addCurrency(CurrencyType.FEATS, count * resource.getBaseFeatsPerMin() * resource.getBaseFeatsCalcRate());
			/*RewardManager.getInstance().grantReward(owner, reward, 
					ModuleInfo.valueOf(ModuleType.CAPTURE_TOWN, SubModuleType.BASE_FEATS_AUTO_REWARD));*/
			baseRewardTime = System.currentTimeMillis();
			
			I18nUtils title = I18nUtils.valueOf(TownConfig.getInstance().OFFLINE_REWARD_MAIL_TITLE.getValue());
			I18nUtils content = I18nUtils.valueOf(TownConfig.getInstance().OFFLINE_REWARD_MAIL_CONTENT.getValue());
			Mail mail = Mail.valueOf(title, content, null, reward);
			MailManager.getInstance().sendMail(mail, playerId);
		}
	}
	
	@JsonIgnore
	public void stopAutoRewardTask() {
		if (autoRewardSelfFuture != null) {
			autoRewardSelfFuture.cancel(false);
		}
	}

	@JsonIgnore 
	public void refresh() { 
		/** 假设是每天来做刷新，只是时间是每一天中的任意时刻 **/
		Date beforeTime = DateUtils.getNextTime(TownConfig.getInstance().getResetAndRewardTime(), new Date(System.currentTimeMillis() - DateUtils.MILLIS_PER_DAY));
		if (lastResetTime < beforeTime.getTime()) {
			dailyBuyCount = 0; 
			dailyCount = 0; 
			clearCDCount = 0;
			nextChallengeTime = 0L; 
			lastResetTime = System.currentTimeMillis();
			owner.getShoppingHistory().refreshSpecialType();
			PacketSendUtility.sendPacket(owner, ShoppingHistoryVO.valueOf(owner));
		}
	}
	
	@JsonIgnore
	public void refreshDaily() {
		if (!DateUtils.isToday(new Date(lastDailyResetTime))) {
			todayTotalReward = 0;
			lastDailyResetTime = System.currentTimeMillis();
		}
	}
	 
	@JsonIgnore 
	public long enterAccTimeCD() { 
		long now = System.currentTimeMillis();
		if (nextChallengeTime != 0L && nextChallengeTime > now) {
			long interval = nextChallengeTime - now;
			nextChallengeTime = now + interval + TownConfig.getInstance().ENTER_ACC_CD_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
		} else {
			nextChallengeTime = now + TownConfig.getInstance().ENTER_ACC_CD_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
		}
		lastEnterTime = now;
		return nextChallengeTime;
	}
	 
	@JsonIgnore 
	public boolean canEnter() { 
		return nextChallengeTime - System.currentTimeMillis() 
				<= TownConfig.getInstance().ACCUMULATE_MAX_CD_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
	}
	
	public int getDailyCount() {
		return dailyCount;
	}

	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}
	
	@JsonIgnore
	public void addDailyCount() {
		dailyCount++;
		EventBusManager.getInstance().submit(TownCaptureEvent.valueOf(owner));
	}
	
	@JsonIgnore
	public void buyDailyCount() {
		dailyCount--;
		dailyBuyCount++;
	}
	
	public int getDailyBuyCount() {
		return dailyBuyCount;
	}

	public void setDailyBuyCount(int dailyBuyCount) {
		this.dailyBuyCount = dailyBuyCount;
	}

	public int getClearCDCount() {
		return clearCDCount;
	}

	public void setClearCDCount(int clearCDCount) {
		this.clearCDCount = clearCDCount;
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	public long getLastEnterTime() {
		return lastEnterTime;
	}

	public void setLastEnterTime(long lastEnterTime) {
		this.lastEnterTime = lastEnterTime;
	}

	public final long getBaseRewardTime() {
		return baseRewardTime;
	}

	public final void setBaseRewardTime(long baseRewardTime) {
		this.baseRewardTime = baseRewardTime;
	}

	public final String getCatpureTownKey() {
		return catpureTownKey;
	}

	public final void setCatpureTownKey(String catpureTownKey) {
		this.catpureTownKey = catpureTownKey;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public long getTodayTotalReward() {
		return todayTotalReward;
	}

	public void setTodayTotalReward(long todayTotalReward) {
		this.todayTotalReward = todayTotalReward;
	}

	public long getLastDailyResetTime() {
		return lastDailyResetTime;
	}

	public void setLastDailyResetTime(long lastDailyResetTime) {
		this.lastDailyResetTime = lastDailyResetTime;
	}

	public long getNextChallengeTime() {
		return nextChallengeTime;
	}

	public void setNextChallengeTime(long nextChallengeTime) {
		this.nextChallengeTime = nextChallengeTime;
	}
	
	@JsonIgnore
	public void clearCDCount() {
		clearCDCount++;
		nextChallengeTime = 0L;
	}

	@JsonIgnore
	public void mergeServer() {
		
	}
}
