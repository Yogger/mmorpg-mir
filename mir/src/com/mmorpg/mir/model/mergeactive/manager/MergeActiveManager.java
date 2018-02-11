package com.mmorpg.mir.model.mergeactive.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.commonactivity.model.CommonActivityPool;
import com.mmorpg.mir.model.commonactivity.resource.CommonFirstPayResource;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.mergeactive.MergeActiveConfig;
import com.mmorpg.mir.model.mergeactive.model.CheapGiftBag;
import com.mmorpg.mir.model.mergeactive.model.LoginGift;
import com.mmorpg.mir.model.mergeactive.model.MergeActive;
import com.mmorpg.mir.model.mergeactive.packet.SM_Draw_Merge_Consume;
import com.mmorpg.mir.model.mergeactive.packet.SM_Express_Next_Opentime;
import com.mmorpg.mir.model.mergeactive.packet.SM_Merge_Cheap_Bag_Reward;
import com.mmorpg.mir.model.mergeactive.packet.SM_Merge_Login_Reward;
import com.mmorpg.mir.model.mergeactive.packet.SM_Temple_Next_Opentime;
import com.mmorpg.mir.model.mergeactive.resource.MergeCheapGiftBagResource;
import com.mmorpg.mir.model.mergeactive.resource.MergeConsumeCompeteResource;
import com.mmorpg.mir.model.mergeactive.resource.MergeLoginGiftResource;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

@Component
public class MergeActiveManager {
	private static MergeActiveManager INSTANCE;

	public static MergeActiveManager getInstance() {
		return INSTANCE;
	}

	@Autowired
	private MergeActiveConfig mergeActiveConfig;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@Autowired
	private ServerState serverState;

	@Static
	public Storage<String, CommonFirstPayResource> firstPayStorage;

	private final List<Date> templeOpenDays = new ArrayList<Date>();

	private final List<Date> expressOpenDays = new ArrayList<Date>();

	@PostConstruct
	void init() {
		INSTANCE = this;
		if (ClearAndMigrate.clear) {
			return;
		}
	}

	public void initAll() {
		openTempleAndExpress();
	}

	public void openTempleAndExpress() {
		String[] templeOpenTime = mergeActiveConfig.TEMPLE_OPEN_TIME.getValue();
		String[] expressOpenTime = mergeActiveConfig.EXPRESS_OPEN_TIME
				.getValue();
		Integer[] templeAndExpressOpenDays = mergeActiveConfig.TEMPLE_AND_EXPRESS_OPEN_DATES
				.getValue();
		Integer aheadMinutes = mergeActiveConfig.TEMPLE_AND_EXPRESS_BROADCAST_AHEAD_MINUTES
				.getValue();
		// 所有的搬砖开启时间
		// final List<Date> templeOpenDays = new ArrayList<Date>();
		// 所有的运镖开启时间
		// final List<Date> expressOpenDays = new ArrayList<Date>();
		for (int i = 0; i < templeAndExpressOpenDays.length; i++) {
			for (int j = 0; j < templeOpenTime.length; j++) {
				Date date = getOpentTime(templeOpenTime[j],
						templeAndExpressOpenDays[i]);
				if (date == null) {
					continue;
				}
				templeOpenDays.add(date);
			}
			for (int k = 0; k < expressOpenTime.length; k++) {
				Date date = getOpentTime(expressOpenTime[k],
						templeAndExpressOpenDays[i]);
				if (date == null) {
					continue;
				}
				expressOpenDays.add(date);
			}
		}
		// 添加搬砖定时事件
		for (Date date : templeOpenDays) {
			simpleScheduler.schedule(new ScheduledTask() {
				public void run() {
					templeBroadCast();
				}

				public String getName() {
					return "国家搬砖广播";
				}
			}, DateUtils.addMinutes(date, 0 - aheadMinutes));
			simpleScheduler.schedule(new ScheduledTask() {
				public void run() {
					templeOpen();
					notifyNextTempleTime(templeOpenDays);
				}

				public String getName() {
					return "国家搬砖开启";
				}
			}, date);
		}
		// 添加运镖定时事件
		for (Date date : expressOpenDays) {
			simpleScheduler.schedule(new ScheduledTask() {
				public void run() {
					expressBroadCast();
				}

				public String getName() {
					return "国家运镖广播";
				}
			}, DateUtils.addMinutes(date, 0 - aheadMinutes));
			simpleScheduler.schedule(new ScheduledTask() {
				public void run() {
					expressOpen();
					notifyNextExpressTime(expressOpenDays);
				}

				public String getName() {
					return "国家运镖开始";
				}
			}, date);
		}
	}

	public void mergeClear(PlayerEnt playerEnt) {
		playerEnt.setMergeActiveJson(JsonUtils.object2String(MergeActive
				.valueOf()));
		if (playerEnt.getActivityJson() == null) {
			return;
		}
		CommonActivityPool activity = JsonUtils.string2Object(
				playerEnt.getActivityJson(), CommonActivityPool.class);
		for (CommonFirstPayResource cfr : firstPayStorage.getAll()) {
			if (cfr.isMergeRemove()) {
				if (activity.getFirstPays() != null) {
					activity.getFirstPays().remove(cfr.getActiveName());
				}
			}
		}
		playerEnt.setActivityJson(JsonUtils.object2String(activity));
	}

	/**
	 * 通过时间（不包含日期）和开服的第几天， 求得开启时间
	 * 
	 * @param time
	 * @return
	 */
	public Date getOpentTime(String time, int mergedDate) {
		Date date = null;
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mergeTime = serverState.getMergeTime();
		// 这里是方便测试， 如果是新服， 这里应该是直接返回
		if (mergeTime == null) {
			// mergeTime = DateUtils.addDays(new Date(), -1);
			return null;
		}
		Date mergedTime = DateUtils.addDays(mergeTime, mergedDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mergedTime);
		StringBuilder builder = new StringBuilder();
		builder.append(calendar.get(Calendar.YEAR)).append("-")
				.append(calendar.get(Calendar.MONTH) + 1).append("-")
				.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ")
				.append(time);
		try {
			date = format.parse(builder.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date.getTime() < System.currentTimeMillis()) {
			return null;
		}
		return date;
	}

	public void templeBroadCast() {
		I18nUtils i18nUtils = I18nUtils.valueOf("601011");
		ChatManager.getInstance().sendSystem(61001, i18nUtils, null);
	}

	public void templeOpen() {
		for (Country country : CountryManager.getInstance().getCountries()
				.values()) {
			country.getCountryQuest().startTemple();
		}
	}

	public void expressBroadCast() {
		I18nUtils i18nUtils = I18nUtils.valueOf("601010");
		ChatManager.getInstance().sendSystem(61001, i18nUtils, null);
	}

	public void expressOpen() {
		for (Country country : CountryManager.getInstance().getCountries()
				.values()) {
			country.getCountryQuest().startExpress();
		}
	}

	public long calNextOpenTime(List<Date> allOpenDays) {
		Date nowTime = new Date();
		Collections.sort(allOpenDays);
		for (Date date : allOpenDays) {
			if (nowTime.compareTo(date) == -1) {
				return date.getTime();
			}
		}
		return 0;
	}

	public List<Date> getTempleOpenDays() {
		return templeOpenDays;
	}

	public List<Date> getExpressOpenDays() {
		return expressOpenDays;
	}

	public void notifyNextTempleTime(List<Date> allOpenDays) {
		for (Country country : CountryManager.getInstance().getCountries()
				.values()) {
			for (Player player : country.getCivils().values()) {
				PacketSendUtility.sendPacket(player, SM_Temple_Next_Opentime
						.valueOf(calNextOpenTime(allOpenDays)));
			}
		}
	}

	public void notifyNextExpressTime(List<Date> allOpenDays) {
		for (Country country : CountryManager.getInstance().getCountries()
				.values()) {
			for (Player player : country.getCivils().values()) {
				PacketSendUtility.sendPacket(player, SM_Express_Next_Opentime
						.valueOf(calNextOpenTime(allOpenDays)));
			}
		}
	}

	public void drawMergeLoginGift(Player player, String id) {
		MergeLoginGiftResource giftResource = mergeActiveConfig.giftResource
				.get(id, true);
		LoginGift loginGift = player.getMergeActive().getLoginGift();
		// 判断领取条件
		if (!giftResource.getCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		// 判断是否已经领取
		if (loginGift.hasDrawBefore(id)) {
			throw new ManagedException(
					ManagedErrorCode.MERGE_ACTIVE_LOGIN_GIFT_HAS_DRAW);
		}
		// 发放奖励
		RewardManager.getInstance().grantReward(
				player,
				Arrays.asList(giftResource.getRewardIds()),
				ModuleInfo.valueOf(ModuleType.MERGE_ACTIVE,
						SubModuleType.LOGIN_GIFT, id));
		// 记录领取情况
		loginGift.addDrawLog(id);
		PacketSendUtility.sendPacket(player, SM_Merge_Login_Reward.valueOf(id));
	}

	public void drawMergeCheapGiftBag(Player player, String giftId) {
		MergeCheapGiftBagResource cheapBagResource = mergeActiveConfig.cheapGiftBagResource
				.get(giftId, true);
		CheapGiftBag cheapGiftBag = player.getMergeActive().getCheapGiftBag();
		// 判断购买条件
		if (!cheapBagResource.getCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		// 判断是否已经购买
		if (cheapGiftBag.hasBuyBefore(giftId)) {
			throw new ManagedException(
					ManagedErrorCode.MERGE_ACTIVE_CHEAP_GIFT_HAS_DRAW);
		}
		// 判断是否已经购买上一级别
		if (!cheapGiftBag.hasBuyLowLevel(cheapBagResource)) {
			throw new ManagedException(
					ManagedErrorCode.MERGE_ACTIVE_CHEAP_GIFT_NO_CONDITION);
		}
		// 判断消耗是否充足
		if (!cheapBagResource.getCoreActions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
		}
		// 开始购买并记录购买日志
		cheapBagResource.getCoreActions().act(
				player,
				ModuleInfo.valueOf(ModuleType.MERGE_ACTIVE,
						SubModuleType.CHEAP_GIFT_BAG, giftId));
		cheapGiftBag.addDrawLog(giftId);
		// 发放奖励
		RewardManager.getInstance().grantReward(
				player,
				Arrays.asList(cheapBagResource.getRewardIds()),
				ModuleInfo.valueOf(ModuleType.MERGE_ACTIVE,
						SubModuleType.CHEAP_GIFT_BAG, giftId));
		// 推送前端
		PacketSendUtility.sendPacket(player, SM_Merge_Cheap_Bag_Reward.valueof(
				cheapBagResource.getGroupId(),
				cheapBagResource.getHighLevelId()));
	}

	public ArrayList<String> getConsumeCanRecievesReward(Player player) {
		ArrayList<String> canRecieves = New.arrayList();
		for (MergeConsumeCompeteResource resource : MergeActiveConfig
				.getInstance().mergeConsumeCompeteResources.getAll()) {
			if (!player.getMergeActive().getConsumeCompete().getRewarded()
					.contains(resource.getId())
					&& resource.getRecieveConditions().verify(player, false)) {
				canRecieves.add(resource.getId());
			}
		}
		return canRecieves;
	}

	public void loginCompensateMail(Player player) {
		for (MergeConsumeCompeteResource resource : MergeActiveConfig
				.getInstance().mergeConsumeCompeteResources.getAll()) {
			if (!player.getMergeActive().getConsumeCompete().getRewarded()
					.contains(resource.getId())
					&& resource.getMailCompensateConditions().verify(player,
							false)) {
				List<String> rewardIds = ChooserManager.getInstance()
						.chooseValueByRequire(player,
								resource.getChooserGroupId());
				Reward reward = RewardManager.getInstance().creatReward(player,
						rewardIds, null);
				Mail mail = Mail.valueOf(
						I18nUtils.valueOf(resource.getMailI18nTitle()),
						I18nUtils.valueOf(resource.getMailI18nContent()), null,
						reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				player.getMergeActive().getConsumeCompete().getRewarded()
						.add(resource.getId());
			}
		}

	}

	public void drawMergeConsumeReward(Player player, String id) {
		MergeConsumeCompeteResource resource = MergeActiveConfig.getInstance()
				.getConsumeCompeteResource(id);

		if (player.getMergeActive().getConsumeCompete().getRewarded()
				.contains(id)) {
			// 该档次奖励已经领取
			throw new ManagedException(
					ManagedErrorCode.MERGE_CONSUME_ALREADY_RECIEVED);
		}

		if (!resource.getRecieveConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getChooserGroupId());
		Reward reward = RewardManager.getInstance().creatReward(player,
				rewardIds, null);
		if (!RewardManager.getInstance().playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		RewardManager.getInstance().grantReward(
				player,
				reward,
				ModuleInfo.valueOf(ModuleType.MERGE_ACTIVE,
						SubModuleType.MERGE_CONSUME_REWARD, id));
		player.getMergeActive().getConsumeCompete().getRewarded()
				.add(resource.getId());

		PacketSendUtility.sendPacket(player, SM_Draw_Merge_Consume.valueOf(id));
	}
}
