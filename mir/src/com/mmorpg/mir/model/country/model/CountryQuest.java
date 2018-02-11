package com.mmorpg.mir.model.country.model;

import java.util.Date;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.countryact.CountryWarEvent;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestOpen;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestStatus;
import com.mmorpg.mir.model.country.packet.SM_Country_War_Event;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.windforce.common.utility.DateUtils;

public class CountryQuest {
	@Transient
	private Country country;
	/** 国家运镖结束时间 */
	private long expressEndTime;
	/** 国家搬砖结束时间 */
	private long templeEndTime;
	/** 国家今天发布国家运镖的次数 */
	private int todayExpressCount;
	/** 国家今天发布国家搬砖的次数 */
	private int todayTempleCount;
	/** 每日发布次数刷新用的时间 */
	private long lastResetTime;
	
	/**
	 * 开始国运
	 */
	public void startExpress() {
		expressEndTime = System.currentTimeMillis() + ConfigValueManager.getInstance().QUEST_EXPRESS_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND;
		todayExpressCount++;
		// 1广播国运开启的消息
		country.sendPackAll(SM_Country_QuestOpen.valueOf((byte) 1, expressEndTime));
		
		for (Country c: CountryManager.getInstance().getCountries().values()) {
			if (!c.getId().equals(country.getId())) {
				c.sendPackAll(SM_Country_War_Event.valueOf(country.getId().getValue(), CountryWarEvent.ENEMY_COUNTRY_EXPRSS.getValue()),
						ConfigValueManager.getInstance().getCountryWarPushCond(CountryWarEvent.ENEMY_COUNTRY_EXPRSS.getValue()));
			}
		}
	}

	/**
	 * 开始国砖
	 */
	public void startTemple() {
		templeEndTime = System.currentTimeMillis() + ConfigValueManager.getInstance().QUEST_TEMPLE_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND;
		// 2广播板砖开启的消息
		todayTempleCount++;
		country.sendPackAll(SM_Country_QuestOpen.valueOf((byte) 2, templeEndTime));
		for (Country c: CountryManager.getInstance().getCountries().values()) {
			if (!c.getId().equals(country.getId())) {
				c.sendPackAll(SM_Country_War_Event.valueOf(country.getId().getValue(), CountryWarEvent.ENEMY_COUNTRY_TEMPLE.getValue()),
						ConfigValueManager.getInstance().getCountryWarPushCond(CountryWarEvent.ENEMY_COUNTRY_TEMPLE.getValue()));
			}
		}
		
	}

	public long getExpressEndTime() {
		return expressEndTime;
	}

	public void setExpressEndTime(long expressEndTime) {
		this.expressEndTime = expressEndTime;
	}

	public long getTempleEndTime() {
		return templeEndTime;
	}

	public void setTempleEndTime(long templeEndTime) {
		this.templeEndTime = templeEndTime;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	@JsonIgnore
	public void setCountry(Country country) {
		this.country = country;
	}

	public int getTodayExpressCount() {
		return todayExpressCount;
	}

	public void setTodayExpressCount(int todayExpressCount) {
		this.todayExpressCount = todayExpressCount;
	}

	public void setTodayTempleCount(int todayTempleCount) {
		this.todayTempleCount = todayTempleCount;
	}

	public int getTodayTempleCount() {
		return todayTempleCount;
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastResetTime))) {
			lastResetTime = System.currentTimeMillis();
			todayExpressCount = 0;
			todayTempleCount = 0;
			country.sendOfficialPack(SM_Country_QuestStatus.valueOf(this, country.getCourt(), !country.getTraitorMapFixs().isEmpty()));
		}
	}
	
	@JsonIgnore
	public void initNewFeature() {
		if (lastResetTime == 0L) { 
			long lastStartExpressTime = expressEndTime - ConfigValueManager.getInstance().QUEST_EXPRESS_TIME.getValue()
					* DateUtils.MILLIS_PER_SECOND;
			if (DateUtils.isToday(new Date(lastStartExpressTime))) {
				todayExpressCount = 1; 
			}
			long lastStartTempleTime = templeEndTime - ConfigValueManager.getInstance().QUEST_TEMPLE_TIME.getValue()
					* DateUtils.MILLIS_PER_SECOND;
			if (DateUtils.isToday(new Date(lastStartTempleTime))) {
				todayTempleCount = 1;
			}
			lastResetTime = System.currentTimeMillis();
		}
	}
	
	@JsonIgnore
	public int getTodayTempleLeftCount() {
		int templeLeft = ConfigValueManager.getInstance().getCountryTechCount(country) - todayTempleCount;
		return templeLeft < 0 ? 0 : templeLeft;  
	}
	
	@JsonIgnore
	public int getTodayExpressLeftCount() {
		int expressLeft = ConfigValueManager.getInstance().getCountryTechCount(country) - todayExpressCount;
		return expressLeft < 0 ? 0 : expressLeft;
	}
	
}
