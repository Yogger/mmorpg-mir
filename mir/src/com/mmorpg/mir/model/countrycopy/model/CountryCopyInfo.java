package com.mmorpg.mir.model.countrycopy.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.windforce.common.utility.DateUtils;

public class CountryCopyInfo {
	/** 今日助威次数 */
	private int encourageCount;
	private long lastEncourageTime;
	/** 今日进入国家副本次数 */
	private int enterCount;
	private long lastEnterTime;
	private long refreshTime;
	@Deprecated
	private int finishCount;
	private int historyEnterCount;
	
	@Transient
	private long lastApplyEncourageTime;

	@Transient
	private List<Effect> effects;
	
	@Transient
	private Player owner;
	
	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(refreshTime))) {
			encourageCount = 0;
			lastEncourageTime = 0;
			enterCount = 0;
			finishCount = 0;
			refreshTime = System.currentTimeMillis();
		}
	}

	@JsonIgnore
	public void addEncourage() {
		encourageCount++;
		lastEncourageTime = System.currentTimeMillis();
	}

	@JsonIgnore
	public void addEnter() {
		enterCount++;
	}
	
	@JsonIgnore
	public void addFinishedCount() {
		addEnter();
		finishCount++;
		historyEnterCount++;
		lastEnterTime = System.currentTimeMillis();
	}

	public int getEncourageCount() {
		return encourageCount;
	}

	public void setEncourageCount(int encourageCount) {
		this.encourageCount = encourageCount;
	}

	public long getLastEncourageTime() {
		return lastEncourageTime;
	}

	public void setLastEncourageTime(long lastEncourageTime) {
		this.lastEncourageTime = lastEncourageTime;
	}

	public int getEnterCount() {
		return enterCount;
	}

	public void setEnterCount(int enterCount) {
		this.enterCount = enterCount;
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public long getLastEnterTime() {
		return lastEnterTime;
	}

	public void setLastEnterTime(long lastEnterTime) {
		this.lastEnterTime = lastEnterTime;
	}

	@JsonIgnore
	public List<Effect> getEffects() {
		return effects;
	}

	@JsonIgnore
	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

	@JsonIgnore
	public void endEffect() {
		if (effects != null && !effects.isEmpty()) {
			for (Effect effect : effects) {
				effect.endEffect();
			}
		}
	}

	@JsonIgnore
	public long getLastApplyEncourageTime() {
		return lastApplyEncourageTime;
	}

	@JsonIgnore
	public void setLastApplyEncourageTime(long lastApplyEncourageTime) {
		this.lastApplyEncourageTime = lastApplyEncourageTime;
	}

	public int getHistoryEnterCount() {
		return historyEnterCount;
	}

	public void setHistoryEnterCount(int historyEnterCount) {
		this.historyEnterCount = historyEnterCount;
	}

	public int getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(int finishCount) {
		this.finishCount = finishCount;
	}

	@JsonIgnore
	public int getLeftCount() {
		int leftCount = CountryCopyConfig.getInstance().getDailyMaxenterCount() - enterCount;
		if (owner.getCountry().getReserveKing().isRealReserveKing(owner.getObjectId())) {
			leftCount += CountryCopyConfig.getInstance().RESERVEKING_ADD_COUNT.getValue();
		}
		return leftCount;
	}
	
	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
}
