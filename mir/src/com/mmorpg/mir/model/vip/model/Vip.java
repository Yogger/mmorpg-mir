package com.mmorpg.mir.model.vip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.operator.model.SuperVip;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.vip.event.FirstPayEvent;
import com.mmorpg.mir.model.vip.event.VipEvent;
import com.mmorpg.mir.model.vip.manager.VipManager;
import com.mmorpg.mir.model.vip.packet.SM_Vip_Update;
import com.mmorpg.mir.model.vip.resource.VipConfig;
import com.mmorpg.mir.model.vip.resource.VipResource;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class Vip {
	public static final StatEffectId VIP_STATE_ID = StatEffectId.valueOf("vip", StatEffectType.VIP);
	/** 临时VIP的ID */
	private static final int TEMPLE_VIP = -1;
	@Transient
	private transient Player owner;
	/** 等级 */
	private int level;
	/** 成长值 */
	private int growthValue;
	/** 已经领取VIP奖励 */
	private ArrayList<Integer> received = new ArrayList<Integer>();
	/** 最后增加活跃度 */
	private transient long lastAddActive;
	/** 最后领取周奖励时间 */
	private long lastRecivedWeekTime;
	/** 下一次领取的时间，后端没有使用，发送给前端显示 */
	private long nextRecivedWeekTime;
	/** 临时VIP结束时间 */
	private long tempVipEndTime;
	/** 是否是超级VIP */
	private boolean superVip;
	/** 最大充值数 */
	private volatile transient long maxCharge;
	private volatile transient long totalCharge;
	/** 未使用的临时VIP时间 */
	private transient long noUseTempVipTime;

	private transient Map<Integer, Long> levelLogTime = New.hashMap();
	/** 充值记录，只是用作订单重复验证 */
	private transient CopyOnWriteArrayList<RechargeHistory> rechargeHistories = new CopyOnWriteArrayList<RechargeHistory>();
	/** 元宝获取记录,这个会影响到充值活动 */
	private transient CopyOnWriteArrayList<GoldRechargeHistory> goldRechargeHistories = new CopyOnWriteArrayList<GoldRechargeHistory>();

	/**
	 * 某一日充值的总额
	 * 
	 * @param time
	 *            某一天的任意时刻
	 * @return
	 */
	@JsonIgnore
	public long dayTotalCharge(Date time) {
		long total = 0;
		for (GoldRechargeHistory rh : goldRechargeHistories) {
			if (DateUtils.isSameDay(new Date(rh.getTime()), time)) {
				total += rh.getGold();
			}
		}
		return total;
	}

	@JsonIgnore
	public long firstChargeTime() {
		int day = VipConfig.getInstance().SUPER_VIP_CIRCLE.getValue();
		if (goldRechargeHistories.isEmpty()) {
			return 0;
		}

		List<GoldRechargeHistory> temp = new ArrayList<GoldRechargeHistory>(goldRechargeHistories.size());
		temp.addAll(goldRechargeHistories);
		long minTime = Long.MAX_VALUE;
		for (GoldRechargeHistory gr : temp) {
			if (gr.getTime() < minTime) {
				minTime = gr.getTime();
			}
		}
		long now = System.currentTimeMillis();
		long removeTime = minTime + (DateUtils.MILLIS_PER_DAY * day);
		while (removeTime < now) {
			List<GoldRechargeHistory> removes = new ArrayList<GoldRechargeHistory>();
			for (GoldRechargeHistory gr : temp) {
				if (gr.getTime() < removeTime) {
					removes.add(gr);
				}
			}
			for (GoldRechargeHistory rm : removes) {
				temp.remove(rm);
			}

			long min = Long.MAX_VALUE;
			if (temp.isEmpty()) {
				return 0;
			}
			for (GoldRechargeHistory gr : temp) {
				if (gr.getTime() < min) {
					min = gr.getTime();
				}
			}
			removeTime = min + (DateUtils.MILLIS_PER_DAY * day);
		}

		return removeTime - (DateUtils.MILLIS_PER_DAY * day);
	}

	/**
	 * 超级VIP，day周期充值总额
	 * 
	 * @param day
	 * @return
	 */
	@JsonIgnore
	public long totalCharge() {
		int day = VipConfig.getInstance().SUPER_VIP_CIRCLE.getValue();
		if (goldRechargeHistories.isEmpty()) {
			return 0;
		}

		List<GoldRechargeHistory> temp = new ArrayList<GoldRechargeHistory>(goldRechargeHistories.size());
		temp.addAll(goldRechargeHistories);
		long minTime = Long.MAX_VALUE;
		for (GoldRechargeHistory gr : temp) {
			if (gr.getTime() < minTime) {
				minTime = gr.getTime();
			}
		}
		long now = System.currentTimeMillis();
		long removeTime = minTime + (DateUtils.MILLIS_PER_DAY * day);
		while (removeTime < now) {
			List<GoldRechargeHistory> removes = new ArrayList<GoldRechargeHistory>();
			for (GoldRechargeHistory gr : temp) {
				if (gr.getTime() < removeTime) {
					removes.add(gr);
				}
			}
			for (GoldRechargeHistory rm : removes) {
				temp.remove(rm);
			}

			long min = Long.MAX_VALUE;
			if (temp.isEmpty()) {
				return 0;
			}
			for (GoldRechargeHistory gr : temp) {
				if (gr.getTime() < min) {
					min = gr.getTime();
				}
			}
			removeTime = min + (DateUtils.MILLIS_PER_DAY * day);
		}

		int total = 0;
		for (GoldRechargeHistory gr : temp) {
			total += gr.getGold();
		}

		return total;
	}

	public static void main(String[] args) {
		Vip vip = new Vip();
		vip.goldRechargeHistories.add(GoldRechargeHistory.valueOf(100, System.currentTimeMillis()));
		vip.goldRechargeHistories.add(GoldRechargeHistory.valueOf(100, System.currentTimeMillis()
				- DateUtils.MILLIS_PER_DAY * 1));
		vip.goldRechargeHistories.add(GoldRechargeHistory.valueOf(100, System.currentTimeMillis()
				- DateUtils.MILLIS_PER_DAY * 3));
		vip.goldRechargeHistories.add(GoldRechargeHistory.valueOf(100, System.currentTimeMillis()
				- DateUtils.MILLIS_PER_HOUR * 12));
		// vip.rechargeHistories.add(RechargeHistory.valueOf(4 + "", 100,
		// System.currentTimeMillis()
		// + DateUtils.MILLIS_PER_HOUR * 12));
		// System.out.println(vip.totalCharge(2));
	}

	@JsonIgnore
	public boolean isVip() {
		return level >= 1;
	}

	@JsonIgnore
	public boolean isSuperVip(SuperVip superVip) {
		if (this.superVip) {
			return true;
		}
		boolean result = false;
		for (GoldRechargeHistory gr : goldRechargeHistories) {
			if (gr.getGold() >= VipConfig.getInstance().SUPER_VIP_ONECHARGE.getValue()) {
				result = true;
				I18nUtils titel18n = I18nUtils.valueOf("superVip_title");
				I18nUtils contextl18n = I18nUtils.valueOf("superVip_content");
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, null);
				MailManager.getInstance().sendMail(mail, owner.getObjectId());
			}
		}
		if (!result) {
			result = (totalCharge() >= VipConfig.getInstance().SUPER_VIP_MINCHARGE.getValue());
		}
		if (result) {
			this.superVip = true;
			PlayerManager.getInstance().updatePlayer(owner);
		}
		return result;
	}

	@JsonIgnore
	public boolean orderRepeat(String orderId) {
		RechargeHistory rh = new RechargeHistory();
		rh.setOrderId(orderId);
		return rechargeHistories.contains(rh);
	}

	@JsonIgnore
	public void addRechargeHistory(String orderId, int gold, long time) {
		rechargeHistories.add(RechargeHistory.valueOf(orderId, gold, time));
	}

	@JsonIgnore
	public void addGoldRechargeHistory(int gold, long time) {
		goldRechargeHistories.add(GoldRechargeHistory.valueOf(gold, time));
		EventBusManager.getInstance().submit(FirstPayEvent.valueOf(owner.getObjectId()));
	}

	@JsonIgnore
	public void addNoUseTempVipTime(long add) {
		noUseTempVipTime += add;
	}

	@JsonIgnore
	public void compareAndSetMaxCharge(long amount) {
		if (amount > maxCharge) {
			maxCharge = amount;
		}
	}

	@JsonIgnore
	public void addTotalCharge(long amount) {
		totalCharge += amount;
	}

	@JsonIgnore
	public void addTempVip() {
		if (noUseTempVipTime == 0) {
			return;
		}
		if (tempVipEndTime < System.currentTimeMillis()) {
			tempVipEndTime = System.currentTimeMillis() + noUseTempVipTime;
		} else {
			tempVipEndTime += noUseTempVipTime;
		}
		noUseTempVipTime = 0;
		PacketSendUtility.sendPacket(owner, SM_Vip_Update.valueOf(this, 0));
	}

	/**
	 * vip成长值刷新
	 */
	@JsonIgnore
	public void refreshLevel() {
		VipResource maxVip = null;
		List<VipResource> levelUpVipResources = new ArrayList<VipResource>();
		for (VipResource vipResource : VipManager.getInstace().getVipResource().getAll()) {
			if (vipResource.getGrowthValue() <= growthValue) {
				if (vipResource.getLevel() > level) {
					levelUpVipResources.add(vipResource);
				}
				if (maxVip == null || maxVip.getGrowthValue() < vipResource.getGrowthValue()) {
					maxVip = vipResource;
				}
			}
		}

		if (maxVip != null && maxVip.getLevel() > level) {
			// int addDoubleSmelt = maxVip.getDoubleSmeltNums() -
			// getResource().getDoubleSmeltNums();
			// int newAvaliable =
			// owner.getEquipmentStorage().addDoubleSmeltNums(addDoubleSmelt);
			// PacketSendUtility.sendPacket(owner,
			// SM_Vip_Avaliable_Smelt.valueOf(newAvaliable));
			EventBusManager.getInstance().submit(VipEvent.valueOf(getOwner(), maxVip, level));
			level = maxVip.getLevel();
			levelLogTime.put(level, System.currentTimeMillis());
			owner.getGameStats().replaceModifiers(VIP_STATE_ID, getResource().getStats(), true);

			// Vip升级时附带在邮件里面的奖励
			Reward allReward = Reward.valueOf();
			for (VipResource vipResource : levelUpVipResources) {
				if (vipResource.getMailRewardChooserGroupId() != null) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(owner,
							vipResource.getMailRewardChooserGroupId());
					Reward reward = RewardManager.getInstance().creatReward(owner, rewardIds, null);
					allReward.addReward(reward);
				}
			}
			// 升级VIP触发的事件
			Map<String, Object> contexts = New.hashMap();
			contexts.put(TriggerContextKey.PLAYER, owner);
			if (!allReward.isEmpty()) {
				contexts.put(TriggerContextKey.MAIL_REWARD, allReward);
			}
			for (String triggerId : maxVip.getEventTriggers()) {
				TriggerManager.getInstance().trigger(contexts, triggerId);
			}

			// TV
			// vip广播20201,20202,20203,20204,20205,20206,20207,20208,20209,20210,20211,20212
			int tvInt = 20200 + level;
			I18nUtils utils = I18nUtils.valueOf(Integer.toString(tvInt));
			utils.addParm("name", I18nPack.valueOf(owner.getName()));
			utils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			ChatManager.getInstance().sendSystem(11001, utils, null);

			// 聊天框
			// 聊天广播
			// 309001,309002,309003,309004,309005,309006,309007,309008,309009,309010,309011,309012
			int chatInt = 309000 + level;
			I18nUtils chatUtils = I18nUtils.valueOf(Integer.toString(chatInt));
			chatUtils.addParm("name", I18nPack.valueOf(owner.getName()));
			chatUtils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			ChatManager.getInstance().sendSystem(0, chatUtils, null);

			// 309051,309052,309053,309054,309055,309056,309057,309058,309059,309060,309061,309062
			int chatCountryInt = 309050 + level;
			I18nUtils chatCountryUtils = I18nUtils.valueOf(Integer.toString(chatCountryInt));
			chatCountryUtils.addParm("name", I18nPack.valueOf(owner.getName()));
			ChatManager.getInstance().sendSystem(6, chatCountryUtils, null, owner.getCountry());

		}
	}

	public void addStats() {
		owner.getGameStats().addModifiers(VIP_STATE_ID, getResource().getStats(), false);
	}

	public static Vip valueOf() {
		Vip vip = new Vip();
		return vip;
	}

	@JsonIgnore
	public Object getField(String field) {
		return getResource().getField(field);
	}

	@JsonIgnore
	public VipResource getResource() {
		if (level == 0 && tempVipEndTime > System.currentTimeMillis()) {
			// 临时VIP
			return VipManager.getInstace().getVipResource(TEMPLE_VIP);
		}
		return VipManager.getInstace().getVipResource(level);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@JsonIgnore
	public void addGrowth(long playerId, int add) {
		growthValue += add;
		refreshLevel();
		PacketSendUtility.sendPacket(owner, SM_Vip_Update.valueOf(this, add));
	}

	public int getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}

	public ArrayList<Integer> getReceived() {
		return received;
	}

	public void setReceived(ArrayList<Integer> received) {
		this.received = received;
	}

	public long getLastAddActive() {
		return lastAddActive;
	}

	public void setLastAddActive(long lastAddActive) {
		this.lastAddActive = lastAddActive;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public long getLastRecivedWeekTime() {
		return lastRecivedWeekTime;
	}

	public void setLastRecivedWeekTime(long lastRecivedWeekTime) {
		this.lastRecivedWeekTime = lastRecivedWeekTime;
	}

	public long getTempVipEndTime() {
		return tempVipEndTime;
	}

	public void setTempVipEndTime(long tempVipEndTime) {
		this.tempVipEndTime = tempVipEndTime;
	}

	public long getNextRecivedWeekTime() {
		return nextRecivedWeekTime;
	}

	public void setNextRecivedWeekTime(long nextRecivedWeekTime) {
		this.nextRecivedWeekTime = nextRecivedWeekTime;
	}

	public long getNoUseTempVipTime() {
		return noUseTempVipTime;
	}

	public void setNoUseTempVipTime(long noUseTempVipTime) {
		this.noUseTempVipTime = noUseTempVipTime;
	}

	public long getMaxCharge() {
		return maxCharge;
	}

	public void setMaxCharge(long maxCharge) {
		this.maxCharge = maxCharge;
	}

	public long getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(long totalCharge) {
		this.totalCharge = totalCharge;
	}

	public Map<Integer, Long> getLevelLogTime() {
		return levelLogTime;
	}

	public void setLevelLogTime(Map<Integer, Long> levelLogTime) {
		this.levelLogTime = levelLogTime;
	}

	@JsonIgnore
	public int getYesterdayLevel() {
		int vipLevel = 0;
		for (Entry<Integer, Long> entry : levelLogTime.entrySet()) {
			if (!DateUtils.isToday(new Date(entry.getValue())) && entry.getKey() > vipLevel) {
				vipLevel = entry.getKey();
			}
		}
		return vipLevel;
	}

	public CopyOnWriteArrayList<RechargeHistory> getRechargeHistories() {
		return rechargeHistories;
	}

	public void setRechargeHistories(CopyOnWriteArrayList<RechargeHistory> rechargeHistories) {
		this.rechargeHistories = rechargeHistories;
	}

	public CopyOnWriteArrayList<GoldRechargeHistory> getGoldRechargeHistories() {
		return goldRechargeHistories;
	}

	public void setGoldRechargeHistories(CopyOnWriteArrayList<GoldRechargeHistory> goldRechargeHistories) {
		this.goldRechargeHistories = goldRechargeHistories;
	}

	public boolean isSuperVip() {
		return superVip;
	}

	public void setSuperVip(boolean superVip) {
		this.superVip = superVip;
	}

	@JsonIgnore
	public int getVipLevelAtThatDay(long time) {
		if (level == 0) {
			return level;
		}
		int maxLevel = 0;
		for (Entry<Integer, Long> entry : levelLogTime.entrySet()) {
			if (DateUtils.isSameDay(new Date(time), new Date(entry.getValue())) || time > entry.getValue()) {
				if (maxLevel < entry.getKey()) {
					maxLevel = entry.getKey();
				}
			}
		}
		return maxLevel;
	}
}
