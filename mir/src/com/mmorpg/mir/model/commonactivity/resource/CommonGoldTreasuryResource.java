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
public class CommonGoldTreasuryResource {
	public static final String ACTIVE_NAME = "ACTIVE_NAME";
	@Id
	private String id;

	@Index(name = ACTIVE_NAME)
	private String activeName;

	private int groupId;

	private int resetTimes;

	private String chooserGroupId;

	private String[] conditonIds;
	/** 消耗Id */
	private String[] actionIds;
	/** 元宝消耗*/
	private String[] goldActionIds;
	/** 电视广播I18N */
	private String tvI18nId;
	/** 电视广播频道 */
	private int tvChannel;
	/** 聊天广播I18N */
	private String chartI18nId;
	/** 聊天广播频道 */
	private int chartChannel;
	/** 记录日志， 道具需要的等级 */
	private int quality;
	/** 大奖id*/
	private String treasureRewardId;
	
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
	private CoreActions goldCoreActions;

	@JsonIgnore
	public CoreActions getGoldCoreActions() {
		if (null == goldCoreActions) {
			goldCoreActions = CoreActionManager.getInstance().getCoreActions(1, goldActionIds);
		}
		return goldCoreActions;
	}
	
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getResetTimes() {
		return resetTimes;
	}

	public void setResetTimes(int resetTimes) {
		this.resetTimes = resetTimes;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
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

	public String[] getGoldActionIds() {
		return goldActionIds;
	}

	public void setGoldActionIds(String[] goldActionIds) {
		this.goldActionIds = goldActionIds;
	}

	public String getTreasureRewardId() {
		return treasureRewardId;
	}

	public void setTreasureRewardId(String treasureRewardId) {
		this.treasureRewardId = treasureRewardId;
	}
	
}
