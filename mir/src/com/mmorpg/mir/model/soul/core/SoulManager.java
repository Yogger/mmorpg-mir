package com.mmorpg.mir.model.soul.core;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.soul.resource.SoulGrowItemResource;
import com.mmorpg.mir.model.soul.resource.SoulResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

/**
 * 英魂数据管理
 * 
 * @author 37wan
 * 
 */
@Component
public class SoulManager implements ISoulManager {

	@Static
	public Storage<Integer, SoulResource> soulStorage;

	@Static
	public Storage<String, SoulGrowItemResource> soulGrowItemStorage;

	/** 清除玩家当前祝福值的时间间隔(单位:小时) */
	@Static("SOUL:CLEAR_BLESSVALUE_INTERVAL_HOUR")
	public ConfigValue<Integer> CLEAR_BLESSVALUE_INTERVAL_HOUR;

	/** 完成这个任务触发开启英魂系统 */
	@Static("SOUL:OPEN_SOUL_SYSTEM_INDEX")
	public ConfigValue<String> OPEN_SOUL_SYSTEM_INDEX;

	@Static("SOUL:BLESSVALUE_CONVER_COUNT")
	public ConfigValue<Integer> BLESSVALUE_CONVER_COUNT;

	/** 计算通过进阶的概率公式 */
	@Static("SOUL:COUNT_SOUL_UPLEVEL_PROB")
	public Formula COUNT_SOUL_UPLEVEL_PROB;

	@Static("SOUL:CRI_ACTIVITY_CHOSERGROUPID")
	public ConfigValue<String> CRI_ACTIVITY_CHOSERGROUPID;

	@Static("SOUL:CRI_ACTIVITY_CONDS")
	private ConfigValue<String[]> CRI_ACTIVITY_CONDS;

	private CoreConditions criActivityConds;

	public CoreConditions getCriActivityConds() {
		if (null == criActivityConds) {
			criActivityConds = CoreConditionManager.getInstance().getCoreConditions(1, CRI_ACTIVITY_CONDS.getValue());
		}
		return criActivityConds;
	}

	@Static("SOUL:BROADCAST_LEAST_RATE")
	public ConfigValue<Integer> BROADCAST_LEAST_RATE;

	public static SoulManager instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static SoulManager getInstance() {
		return instance;
	}

	public long getIntervalTime() {
		return CLEAR_BLESSVALUE_INTERVAL_HOUR.getValue() * DateUtils.MILLIS_PER_HOUR;
	}

	/**
	 * 获取当前阶段英魂的配置数据
	 * 
	 * @param level
	 *            当前阶段
	 * @return SoulResource
	 */
	public SoulResource getSoulResource(int level) {
		return soulStorage.get(level, true);
	}

	public boolean isOpen(Player player) {
		return ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.SOUL_PF);
	}

	// 周暴击活动
	@Static("SOUL:WEEK_CRI_CHOOSER_ID")
	public ConfigValue<String> WEEK_CRI_CHOOSER_ID;
}
