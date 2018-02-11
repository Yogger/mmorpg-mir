package com.mmorpg.mir.model.artifact.core;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.artifact.resource.ArtifactGrowItemResource;
import com.mmorpg.mir.model.artifact.resource.ArtifactResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

/**
 * 神兵数据管理
 * 
 * @author 37wan
 * 
 */
@Component
public class ArtifactManager implements IArtifactManager {

	@Static
	public Storage<Integer, ArtifactResource> artifactStorage;

	@Static
	public Storage<String, ArtifactGrowItemResource> artifactGrowItemStorage;

	/** 清除玩家当前祝福值的时间间隔(单位:小时) */
	@Static("ARTIFACT:CLEAR_BLESSVALUE_INTERVAL_HOUR")
	public ConfigValue<Integer> CLEAR_BLESSVALUE_INTERVAL_HOUR;

	/** 完成这个任务触发开启英魂系统 */
	@Static("ARTIFACT:OPEN_ARTIFACT_SYSTEM_INDEX")
	public ConfigValue<String> OPEN_ARTIFACT_SYSTEM_INDEX;

	/** 计算进阶成功的概率公式 */
	@Static("ARTIFACT:COUNT_ARTIFACT_UPLEVEL_PROB")
	public Formula COUNT_ARTIFACT_UPLEVEL_PROB;

	@Static("ARTIFACT:BUFF_STATS")
	public ConfigValue<Stat[]> BUFF_STATS;

	@Static("ARTIFACT:BUFF_BUY_ACT")
	public ConfigValue<String[]> BUFF_BUY_ACT;

	@Static("ARTIFACT:BUFF_BUY_REWARDID")
	public ConfigValue<String> BUFF_BUY_REWARDID;

	@Static("ARTIFACT:BLESSVALUE_CONVER_COUNT")
	public ConfigValue<Integer> BLESSVALUE_CONVER_COUNT;

	@Static("ARTIFACT:CRI_ACTIVITY_CHOSERGROUPID")
	public ConfigValue<String> CRI_ACTIVITY_CHOSERGROUPID;

	@Static("ARTIFACT:CRI_ACTIVITY_CONDS")
	private ConfigValue<String[]> CRI_ACTIVITY_CONDS;

	private CoreConditions criActivityConds;

	public CoreConditions getCriActivityConds() {
		if (null == criActivityConds) {
			criActivityConds = CoreConditionManager.getInstance().getCoreConditions(1, CRI_ACTIVITY_CONDS.getValue());
		}
		return criActivityConds;
	}

	@Static("ARTIFACT:BROADCAST_LEAST_RATE")
	public ConfigValue<Integer> BROADCAST_LEAST_RATE;

	private static ArtifactManager instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static ArtifactManager getInstance() {
		return instance;
	}

	public long getIntervalTime() {
		return CLEAR_BLESSVALUE_INTERVAL_HOUR.getValue() * DateUtils.MILLIS_PER_HOUR;
	}

	private CoreActions buffBuyActions;

	public CoreActions getBuffBuyActions() {
		if (buffBuyActions == null) {
			buffBuyActions = CoreActionManager.getInstance().getCoreActions(1, BUFF_BUY_ACT.getValue());
		}
		return buffBuyActions;
	}

	/**
	 * 获取当前阶段英魂的配置数据
	 * 
	 * @param level
	 *            当前阶段
	 * @return ArtifactResource
	 */
	public ArtifactResource getArtifactResource(int level) {
		return artifactStorage.get(level, true);
	}

	public boolean isOpen(Player player) {
		return ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.ARTIFACT);
	}

	// 周暴击活动
	@Static("ARTIFACT:WEEK_CRI_CHOOSER_ID")
	public ConfigValue<String> WEEK_CRI_CHOOSER_ID;
}
