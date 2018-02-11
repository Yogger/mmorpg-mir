package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonIdentifyTreasureResource {
	public static final String ACTIVE_NAME = "ACTIVE_NAME";

	@Id
	private String id;
	@Index(name = ACTIVE_NAME, unique = true)
	private String activeName;
	/** 奖池 */
	private String chooserGroupId;
	/** 单次鉴宝好运值 */
	private int perLuckValue;
	/** 最大好运值 */
	private int maxLuckValue;
	/** 奖池中的最大奖励 */
	private String treasureRewardId;
	/** 鉴宝条件 */
	private String[] conditonIds;
	/** 开启鉴宝条件*/
	private String[] openConditionIds;
	/** 消耗Id */
	private String[] actionIds;
	/** 电视广播I18N */
	private String tvI18nId;
	/** 电视广播频道 */
	private int tvChannel;
	/** 聊天广播I18N */
	private String chartI18nId;
	/** 聊天广播频道 */
	private int chartChannel;
	/** 记录日志， 道具需要的等级*/
	private int quality;
	/** 活动的类型*/
	private int activeType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public int getPerLuckValue() {
		return perLuckValue;
	}

	public void setPerLuckValue(int perLuckValue) {
		this.perLuckValue = perLuckValue;
	}

	public int getMaxLuckValue() {
		return maxLuckValue;
	}

	public void setMaxLuckValue(int maxLuckValue) {
		this.maxLuckValue = maxLuckValue;
	}

	public String getTreasureRewardId() {
		return treasureRewardId;
	}

	public void setTreasureRewardId(String treasureRewardId) {
		this.treasureRewardId = treasureRewardId;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}

	public String[] getActionIds() {
		return actionIds;
	}

	public void setActionIds(String[] actionIds) {
		this.actionIds = actionIds;
	}

	public String getTvI18nId() {
		return tvI18nId;
	}

	public void setTvI18nId(String tvI18nId) {
		this.tvI18nId = tvI18nId;
	}

	public int getTvChannel() {
		return tvChannel;
	}

	public void setTvChannel(int tvChannel) {
		this.tvChannel = tvChannel;
	}

	public String getChartI18nId() {
		return chartI18nId;
	}

	public void setChartI18nId(String chartI18nId) {
		this.chartI18nId = chartI18nId;
	}

	public int getChartChannel() {
		return chartChannel;
	}

	public void setChartChannel(int chartChannel) {
		this.chartChannel = chartChannel;
	}


	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public String[] getOpenConditionIds() {
		return openConditionIds;
	}

	public void setOpenConditionIds(String[] openConditionIds) {
		this.openConditionIds = openConditionIds;
	}
	
	@Transient
	private CoreConditions coreConditions;

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (null == coreConditions) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditonIds);
		}
		return coreConditions;
	}

	@Transient
	private CoreActions coreActions;

	@JsonIgnore
	public CoreActions getCoreActions() {
		if (null == coreActions) {
			coreActions = CoreActionManager.getInstance().getCoreActions(1, actionIds);
		}
		return coreActions;
	}
	
	@Transient
	private CoreConditions openCoreConditions;

	@JsonIgnore
	public CoreConditions getCoreOpenConditions() {
		if (null == openCoreConditions) {
			openCoreConditions = CoreConditionManager.getInstance().getCoreConditions(1, openConditionIds);
		}
		return openCoreConditions;
	}

	public int getActiveType() {
		return activeType;
	}

	public void setActiveType(int activeType) {
		this.activeType = activeType;
	}
}
