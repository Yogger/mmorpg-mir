package com.mmorpg.mir.model.welfare.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.welfare.manager.OfflineManager;
import com.mmorpg.mir.model.welfare.manager.WelfareConfigValueManager;
import com.windforce.common.utility.DateUtils;

/**
 * 离线经验
 * 
 * @author 37wan
 * 
 */
public class OfflineExp {

	private long loginoutTimeCount;// 累计离线时间
	
	public void refresh(Player player) {
		Date loginTime = player.getPlayerEnt().getStat().getLastLogin();
		Date loginoutTime = player.getPlayerEnt().getStat().getLastLogoutTime();
		// 计算累计离线时间
		addLoginoutTimeCount(player, loginTime.getTime() - loginoutTime.getTime());
	}

	public long getLoginoutTimeCount() {
		return loginoutTimeCount;
	}

	public void setLoginoutTimeCount(long loginoutTimeCount) {
		this.loginoutTimeCount = loginoutTimeCount;
	}

	@JsonIgnore
	public long addLoginoutTimeCount(Player player, long loginoutTimeCount) {
		// String[] conds = ActiveManager.getInstance().getActiveResource(ActiveEnum.ACTIVE_SIGN.getEventId()).getOpenConditions();
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, "opmk32")) {
			return 0L;
		}
		
		this.loginoutTimeCount += loginoutTimeCount;
		long maxInterval = WelfareConfigValueManager.getInstance().OFFLINE_MAX_TIME_OUT.getValue()
				* DateUtils.MILLIS_PER_HOUR;
		if (this.loginoutTimeCount >= maxInterval) {
			this.loginoutTimeCount = maxInterval;
		}
		return this.loginoutTimeCount;
	}

	@Transient
	private Map<Integer, Boolean> rewardMap = New.hashMap();

	@JsonIgnore
	public void reward(int type) {
		rewardMap.put(new Integer(type), true);
	}

	/** 已经领奖 */
	@JsonIgnore
	public boolean rewarded(int type) {
		return rewardMap.containsKey(new Integer(type));
	}

	/** 是否能领奖 */
	@JsonIgnore
	public boolean canReward(int type) {
		return !rewardMap.containsKey(new Integer(type));
	}

	@JsonIgnore
	public void clear() {
		loginoutTimeCount = 0;
		rewardMap.clear();
	}

	/** 累计离线时间 */
	@JsonIgnore
	public int getOfflineCountSeconds() {
		long unit = OfflineManager.getInstance().OFFLINE_EXP_BASIC_UNIT.getValue() * 1000L;
		long unitCount = getLoginoutTimeCount() / unit;
		long result = unitCount * unit / 1000L;
		return (int) result; 
	}

}
