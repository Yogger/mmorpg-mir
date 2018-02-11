package com.mmorpg.mir.model.welfare.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.model.BossHistory;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.manager.FirstPayManager;
import com.mmorpg.mir.model.welfare.manager.WelfareConfigValueManager;
import com.mmorpg.mir.model.welfare.packet.SM_Draw_SevenDay_Reward_Result;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Finish_First_Pay;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_FirstPay;
import com.windforce.common.utility.DateUtils;

/**
 * 福利大厅
 * 
 * @author 37wan
 * 
 */
public class Welfare {

	public static StatEffectId MINICLIENT_EXP = StatEffectId.valueOf("MINI_CLIENT_ADDITION", StatEffectType.WELFARE);

	/** 签到 */
	private Sign sign;
	/** 在线奖励 */
	private OnlineReward onlineReward;
	/** 离线经验 */
	private OfflineExp offlineExp;
	/** 活跃值 */
	private ActiveValue activeValue;
	/** 老的收益追回 */
	private Clawback oldlawback;
	/** 新的收益追回 */
	private Clawback newClawback;
	//
	private WelfareHistory welfareHistory;
	/** 对应的一次性奖励是否领取 */
	private ArrayList<Boolean> oneOffGift;
	// 上次上香的时间
	private ArrayList<Long> feteTime;
	// 历史上香次数
	private int historyFeteCount;
	/** 7天奖励 */
	private SevenDayReward sevenDayReward;
	/** 是否完成首充 */
	private boolean finishFirstPay;

	/** 是否领取了首充奖励 */
	private boolean drawVipfirstPayReward;

	private NonBlockingHashSet<BossGift> bossGiftSet;

	/** 礼金汇总 */
	private GiftCollect giftCollect;

	private Integer accLoginDays;

	private Long lastAccLoginNumberTime;

	public static Welfare valueOf(Player owner) {
		Welfare welfare = new Welfare();
		welfare.init(owner);
		return welfare;
	}

	/**
	 * 领取首充奖励
	 * 
	 * @param player
	 */
	public void drawFirstPayReward(Player player) {
		if (!finishFirstPay) {
			// 没有完成首充
			throw new ManagedException(ManagedErrorCode.FIRSTPAY_NOT_PAY);
		}
		if (drawVipfirstPayReward) {
			// vip首充奖励已经领取
			throw new ManagedException(ManagedErrorCode.FIRSTPAY_REWARD_HAS_DRAW);
		}

		String chooserId = FirstPayManager.getInstance().getVipFirstPayReward();
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, chooserId);
		RewardManager.getInstance().grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.VIP_FIRST_PAY, SubModuleType.FIRST_PAY));
		drawVipfirstPayReward = true;
		player.getArtifact().updateAppLevel(1);
		// 发个神兵buff的奖励
		RewardManager.getInstance().grantReward(player,
				WelfareConfigValueManager.getInstance().ARTIFACT_BUFF_REWARD_ID.getValue(),
				ModuleInfo.valueOf(ModuleType.VIP_FIRST_PAY, SubModuleType.FIRST_PAY));

		I18nUtils i18nUtils = I18nUtils.valueOf("80001");
		i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
		i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

		i18nUtils = I18nUtils.valueOf("310001");
		i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
		i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(0, i18nUtils, null);

		I18nUtils chatUtils = I18nUtils.valueOf("310011");
		chatUtils.addParm("name", I18nPack.valueOf(player.getName()));
		ChatManager.getInstance().sendSystem(6, chatUtils, null, player.getCountry());
		PacketSendUtility.sendPacket(player,
				SM_Welfare_FirstPay.valueOf(this.finishFirstPay, this.drawVipfirstPayReward));
	}

	/**
	 * 领取7天登录奖励
	 * 
	 * @param dayIndex
	 */
	public void drawSevenDayOnlineReward(Player player, int dayIndex) {
		if (!sevenDayReward.getSevendayDrawRecord().containsKey(dayIndex)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (sevenDayReward.isDayRewarded(dayIndex)) {
			throw new ManagedException(ManagedErrorCode.TARGET_DAY_ONLINE_REWARD_HAS_DRAW);
		}
		String rewardChooser = WelfareConfigValueManager.getInstance().SEVEN_DAY_ONLINE_REWARD_CHOOSERIDS.getValue()[dayIndex - 1];

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, rewardChooser);

		RewardManager.getInstance().grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.SEVENDAYREWARD, SubModuleType.SEVEN_LOGIN));
		sevenDayReward.drawReward(dayIndex);

		if (dayIndex != 1) {
			// 80003,80004,80005,80006,80007,80008
			int resourceId = 80001 + dayIndex;
			I18nUtils i18nUtils = I18nUtils.valueOf(Integer.toString(resourceId));
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

			// 310003,10004,10005,10006,10007,10008
			resourceId = 310001 + dayIndex;
			i18nUtils = I18nUtils.valueOf(Integer.toString(resourceId));
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(0, i18nUtils, null);
		}

		PacketSendUtility.sendPacket(player,
				SM_Draw_SevenDay_Reward_Result.valueOf(sevenDayReward.getSevendayDrawRecord()));
	}

	// 福利大厅初始化
	private void init(Player owner) {
		setSign(Sign.valueOf(owner));
		setOnlineReward(new OnlineReward());
		setOfflineExp(new OfflineExp());
		setActiveValue(ActiveValue.valueOf(owner));
		setOldlawback(new Clawback());
		setNewClawback(new Clawback());
		setWelfareHistory(new WelfareHistory());
		setOneOffGift(new ArrayList<Boolean>(WelfareConfigValueManager.getInstance().getAllOneOffGiftResources().size()));
		setSevenDayReward(SevenDayReward.valueOf());
		setFeteTime(new ArrayList<Long>());
		bossGiftSet = new NonBlockingHashSet<BossGift>();
		finishFirstPay = false;
		drawVipfirstPayReward = true;
		accLoginDays = owner.getPlayerStat().getAccLoginNumber();
		lastAccLoginNumberTime = owner.getPlayerStat().getLastAccLoginNumberTime();
		setGiftCollect(GiftCollect.valueOf());
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public OnlineReward getOnlineReward() {
		return onlineReward;
	}

	public void setOnlineReward(OnlineReward onlineReward) {
		this.onlineReward = onlineReward;
	}

	public OfflineExp getOfflineExp() {
		return offlineExp;
	}

	public void setOfflineExp(OfflineExp offlineExp) {
		this.offlineExp = offlineExp;
	}

	public ActiveValue getActiveValue() {
		return activeValue;
	}

	public void setActiveValue(ActiveValue activeValue) {
		this.activeValue = activeValue;
	}

	public Clawback getOldlawback() {
		return oldlawback;
	}

	public void setOldlawback(Clawback oldlawback) {
		this.oldlawback = oldlawback;
	}

	public Clawback getNewClawback() {
		return newClawback;
	}

	public void setNewClawback(Clawback newClawback) {
		this.newClawback = newClawback;
	}

	public WelfareHistory getWelfareHistory() {
		return welfareHistory;
	}

	public void setWelfareHistory(WelfareHistory welfareHistory) {
		this.welfareHistory = welfareHistory;
	}

	public ArrayList<Boolean> getOneOffGift() {
		return oneOffGift;
	}

	public void setOneOffGift(ArrayList<Boolean> oneOffGift) {
		this.oneOffGift = oneOffGift;
	}

	@JsonIgnore
	public boolean oneOffGiftIsReward(int index) {
		if (index >= oneOffGift.size()) {
			int dif = index - oneOffGift.size();
			while (dif-- >= 0) {
				oneOffGift.add(false);
			}
			return false;
		}
		return oneOffGift.get(index);
	}

	@JsonIgnore
	public void rewardOneOffGiftIsReward(int index) {
		if (!oneOffGiftIsReward(index))
			oneOffGift.set(index, true);
	}

	public ArrayList<Long> getFeteTime() {
		return feteTime;
	}

	public void setFeteTime(ArrayList<Long> feteTime) {
		this.feteTime = feteTime;
	}

	public int getHistoryFeteCount() {
		return historyFeteCount;
	}

	public void setHistoryFeteCount(int historyFeteCount) {
		this.historyFeteCount = historyFeteCount;
	}

	@JsonIgnore
	public void addFeteHistoryCount(int addCount) {
		if (feteTime == null) {
			feteTime = new ArrayList<Long>();
			feteTime.add(System.currentTimeMillis());
		} else {
			feteTime.clear();
			feteTime.add(System.currentTimeMillis());
		}
		this.historyFeteCount += addCount;
	}

	@JsonIgnore
	public long getLastFeteTime() {
		if (feteTime.isEmpty()) {
			return 0L;
		}
		return feteTime.get(feteTime.size() - 1);
	}

	public NonBlockingHashSet<BossGift> getBossGiftSet() {
		return bossGiftSet;
	}

	public void setBossGiftSet(NonBlockingHashSet<BossGift> bossGiftSet) {
		this.bossGiftSet = bossGiftSet;
	}

	@JsonIgnore
	public ArrayList<BossGift> getSortedGiftList() {
		clearDeprecatedGift();
		ArrayList<BossGift> list = new ArrayList<BossGift>(bossGiftSet);
		Collections.sort(list, new Comparator<BossGift>() {
			@Override
			public int compare(BossGift o1, BossGift o2) {
				return (int) (o1.getDeadTime() - o2.getDeadTime());
			}
		});
		return list;
	}

	@JsonIgnore
	private void clearDeprecatedGift() {
		ArrayList<BossGift> removeList = New.arrayList();
		for (BossGift b : bossGiftSet) {
			SpawnGroupResource resource = SpawnManager.getInstance().getSpawn(b.getSpawnKey());
			BossResource bossResource = BossManager.getInstance().getBossResource(resource.getBossResourceId(), true);
			BossHistory history = BossManager.getInstance().loadOrCreateBossEntity(bossResource);
			if (!history.getGiftInfoMap().containsKey(b.getDeadTime())) {
				removeList.add(b);
				continue;
			}
			if (!DateUtils.isToday(new Date(b.getDeadTime()))) {
				removeList.add(b);
			}
		}
		for (BossGift c : removeList) {
			bossGiftSet.remove(c);
		}
	}

	/**
	 * 完成首次充值还没领奖
	 */
	public void finishFirstPay(Player player) {
		this.finishFirstPay = true;
		this.drawVipfirstPayReward = false;
		PacketSendUtility
				.sendPacket(player, SM_Welfare_Finish_First_Pay.valueOf(finishFirstPay, drawVipfirstPayReward));
	}

	public boolean isFinishFirstPay() {
		return finishFirstPay;
	}

	public void setFinishFirstPay(boolean finishFirstPay) {
		this.finishFirstPay = finishFirstPay;
	}

	public boolean isDrawVipfirstPayReward() {
		return drawVipfirstPayReward;
	}

	public void setDrawVipfirstPayReward(boolean drawVipfirstPayReward) {
		this.drawVipfirstPayReward = drawVipfirstPayReward;
	}

	public SevenDayReward getSevenDayReward() {
		return sevenDayReward;
	}

	public void setSevenDayReward(SevenDayReward sevenDayReward) {
		this.sevenDayReward = sevenDayReward;
	}

	public GiftCollect getGiftCollect() {
		return giftCollect;
	}

	public void setGiftCollect(GiftCollect giftCollect) {
		this.giftCollect = giftCollect;
	}

	public Integer getAccLoginDays() {
		return accLoginDays;
	}

	public Long getLastAccLoginNumberTime() {
		return lastAccLoginNumberTime;
	}

	public void setAccLoginDays(Integer accLoginDays) {
		this.accLoginDays = accLoginDays;
	}

	public void setLastAccLoginNumberTime(Long lastAccLoginNumberTime) {
		this.lastAccLoginNumberTime = lastAccLoginNumberTime;
	}

}
