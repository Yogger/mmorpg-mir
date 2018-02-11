package com.mmorpg.mir.model.country.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Transient;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.event.CountryTechnologyUpgradeEvent;
import com.mmorpg.mir.model.country.packet.SM_CountryTechnology_BuildValue_Change;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestStatus;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.country.resource.CountryTechnologyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.MailGroup;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

/**
 * 新国家科技
 * 
 * @author Kuang Hao
 * @since v1.0 2015-10-13
 * 
 */
public class NewTechnology {
	@Transient
	private transient Country country;
	/** 建设值 */
	private int buildValue;
	/** 等级 */
	private volatile int grade;
	/*
	 * TODO 当开启军旗1时，默认存有5面军旗，每放置1面军旗则储量减1，储量为0时不可放置军旗 
	 * 当军旗储量未达上限时，每30分钟存储1面军旗，最多可存储5面军旗（存储时间和存储上 ，建议用CountryFacade的心跳做
	 */
	/** 军旗数量 */
	private volatile int flagCount;
	/** 军旗数量最后刷新时间 */
	private volatile long lastFlagCountRefreshTime;
	/** 最近放置军旗的玩家 */
	private volatile long lastPlaceFlagPlayerId;

	// 构造函数
	public static NewTechnology valueOf(int flagCount) {
		NewTechnology technology = new NewTechnology();
		technology.flagCount = flagCount;
		technology.lastFlagCountRefreshTime = System.currentTimeMillis();
		technology.lastPlaceFlagPlayerId = 0L;
		return technology;
	}

	@JsonIgnore
	public void addBuildValue(Player player, int value) {
		if (isReachMaxBuildValue()) {
			PacketSendUtility.sendPacket(player,
					SM_CountryTechnology_BuildValue_Change.valueOf(this.grade, 0, this.buildValue, false));
			return;
		}

		int buildValueOld = this.buildValue;
		upgrade(this.buildValue + value);
		PacketSendUtility.sendPacket(player, SM_CountryTechnology_BuildValue_Change.valueOf(this.grade, this.buildValue
				- buildValueOld, this.buildValue, false));
	}

	@JsonIgnore
	public void fixBuildValue(int buildValueFix) {
		if (isReachMaxBuildValue()) {
			return;
		}
		int buildValueOld = this.buildValue;
		boolean sendAll = upgrade(buildValueFix);
		if (!sendAll) {
			country.sendPackAll(SM_CountryTechnology_BuildValue_Change.valueOf(this.grade, this.buildValue
					- buildValueOld, this.buildValue, true));
		}
	}

	@JsonIgnore
	private boolean upgrade(int buildValueNew) {
		int buildValueOld = this.buildValue;
		int maxBuildValue = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_MAX_BUILDVALUE.getValue();
		this.buildValue = (buildValueNew > maxBuildValue) ? maxBuildValue : buildValueNew;

		List<CountryTechnologyResource> storage = new ArrayList<CountryTechnologyResource>(
				ConfigValueManager.getInstance().countryTechnologyStorage.getAll());
		Collections.sort(storage, new CountryTechnologyResource.GradeComparator());

		int newGrade = grade;
		Set<Integer> upgradeIds = new TreeSet<Integer>();
		for (CountryTechnologyResource resource : storage) {
			if (resource.getGrade() <= this.grade) {
				continue;
			}
			if (this.buildValue < resource.getBuildValue()) {
				break;
			}
			newGrade = resource.getGrade();
			upgradeIds.add(resource.getGrade());
		}
		boolean sendAll = false;
		if (newGrade != this.grade) {
			this.grade = newGrade;
			broadCast(upgradeIds);
			sendUpgradeMail(upgradeIds);
			country.sendPackAll(SM_CountryTechnology_BuildValue_Change.valueOf(this.grade, this.buildValue
					- buildValueOld, this.buildValue, true));
			sendAll = true;
			country.sendOfficialPack(SM_Country_QuestStatus.valueOf(country.getCountryQuest(), country.getCourt(),
					!country.getTraitorMapFixs().isEmpty()));
			for (Player pid : country.getCivils().values()) {
				EventBusManager.getInstance().submit(CountryTechnologyUpgradeEvent.valueOf(pid.getObjectId()));
			}
		}
		return sendAll;
	}

	@JsonIgnore
	private void broadCast(Set<Integer> upgradeIds) {
		for (Integer upgradeId : upgradeIds) {
			// 404101、404102、404103、404104、404105
			int iId = 404100 + upgradeId;
			I18nUtils utils = I18nUtils.valueOf(Integer.toString(iId));
			utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(country.getName()));
			ChatManager.getInstance().sendSystem(71001, utils, null);

			// 301031、301032、301033、301034、301035
			int cid = 301030 + upgradeId;
			I18nUtils chatUtils = I18nUtils.valueOf(Integer.toString(cid));
			ChatManager.getInstance().sendSystem(6, chatUtils, null, country);
		}
	}

	@JsonIgnore
	private void sendUpgradeMail(Set<Integer> upgradeIds) {
		for (Integer gradeId : upgradeIds) {
			CountryTechnologyResource resource = ConfigValueManager.getInstance().countryTechnologyStorage.get(gradeId,
					true);
			Reward reward = RewardManager.getInstance().creatReward(null, resource.getRewardId(), null);

			I18nUtils titel18n = I18nUtils.valueOf(resource.getMailTitleIl18n());
			I18nUtils contextl18n = I18nUtils.valueOf(resource.getMailContentIl18n());

			CoreConditionResource countryConditionResource = CoreConditionResource.createCondition(
					CoreConditionType.COUNTRY_COND, "", country.getId().getValue());
			long endTime = System.currentTimeMillis() + 3650 * +DateUtils.MILLIS_PER_DAY;
			MailGroup mailGroup = MailGroup.valueOf(titel18n, contextl18n, null,
					new CoreConditionResource[] { countryConditionResource }, reward, endTime);
			MailManager.getInstance().addMailGroup(mailGroup);
		}
		for (Player player : country.getCivils().values()) {
			MailManager.getInstance().receiveGroupMail(player.getObjectId());
		}
	}

	@JsonIgnore
	public boolean refreshFlagCount() {
		int flagCountMax = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_MAX_COUNT.getValue();
		if (this.flagCount == flagCountMax || !isTimeNeedRefresh()) {
			return false;
		}
		long createPeriod = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_CREATE_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND;
		int flagCountAdd = (int) ((System.currentTimeMillis() - this.lastFlagCountRefreshTime) / createPeriod);
		int flagCountOld = this.flagCount;
		this.flagCount = (this.flagCount + flagCountAdd) > flagCountMax ? flagCountMax : this.flagCount + flagCountAdd;
		this.lastFlagCountRefreshTime += (this.flagCount - flagCountOld) * createPeriod;
		return true;
	}

	@JsonIgnore
	public boolean isTimeNeedRefresh() {
		if (System.currentTimeMillis() - this.lastFlagCountRefreshTime < ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_CREATE_TIME
				.getValue() * DateUtils.MILLIS_PER_SECOND) {
			return false;
		}
		return true;
	}

	@JsonIgnore
	public long getNextFlagCDTime() {
		long createPeriod = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_CREATE_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND;
		long flagCountCDTime = this.lastFlagCountRefreshTime + createPeriod;
		return flagCountCDTime;
	}

	@JsonIgnore
	public void placeFlag(long playerId) {
		int flagCountMax = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_MAX_COUNT.getValue();
		if (this.flagCount == flagCountMax) {
			this.lastFlagCountRefreshTime = System.currentTimeMillis();
		}
		this.lastPlaceFlagPlayerId = playerId;
		this.flagCount--;
	}

	@JsonIgnore
	public String getLastPlaceFlagPlayerName() {
		if (this.lastPlaceFlagPlayerId == 0L) {
			return "";
		}
		Player player = PlayerManager.getInstance().getPlayer(this.lastPlaceFlagPlayerId);
		if (player == null) {
			return "";
		}
		return player.getName();
	}

	@JsonIgnore
	private boolean isReachMaxBuildValue() {
		int maxBuildValue = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_MAX_BUILDVALUE.getValue();
		return this.buildValue >= maxBuildValue;
	}

	// getter - setter
	public int getGrade() {
		return grade;
	}

	public int getBuildValue() {
		return buildValue;
	}

	public void setBuildValue(int buildValue) {
		this.buildValue = buildValue;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getFlagCount() {
		return flagCount;
	}

	public void setFlagCount(int flagCount) {
		this.flagCount = flagCount;
	}

	public long getLastFlagCountRefreshTime() {
		return lastFlagCountRefreshTime;
	}

	public void setLastFlagCountRefreshTime(long lastFlagCountRefreshTime) {
		this.lastFlagCountRefreshTime = lastFlagCountRefreshTime;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	@JsonIgnore
	public void setCountry(Country country) {
		this.country = country;
	}

	public long getLastPlaceFlagPlayerId() {
		return lastPlaceFlagPlayerId;
	}

	public void setLastPlaceFlagPlayerId(long lastPlaceFlagPlayerId) {
		this.lastPlaceFlagPlayerId = lastPlaceFlagPlayerId;
	}

}
