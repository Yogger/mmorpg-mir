package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonConsumeGiftResource {
	public static final String ACTIVITY_NAME = "ACTIVITY_NAME";
	/** spawnId */
	@Id
	private String id;
	@Index(name = ACTIVITY_NAME)
	private String activeName;
	/** 组Id */
	private Integer groupId;
	/** 消费的元宝 */
	private int consumeGold;
	/** 领奖条件 */
	private String[] conditonIds;
	/** 奖励的choosergroup id */
	private String chooserGroupId;
	/** 开启首充的条件 */
	private String[] openConditionIds;
	/** 结束后领取条件 */
	private String[] endConditionIds;
	/** 电视广播I18N */
	private String tvI18nId;
	/** 电视广播频道 */
	private int tvChannel;
	/** 聊天广播I18N */
	private String chartI18nId;
	/** 聊天广播频道 */
	private int chartChannel;
	/** 邮件I18n的标题 */
	private String i18nTitle;
	/** 邮件内容*/
	private String i18nContent;

	public String getI18nTitle() {
		return i18nTitle;
	}

	public void setI18nTitle(String i18nTitle) {
		this.i18nTitle = i18nTitle;
	}

	public String getI18nContent() {
		return i18nContent;
	}

	public void setI18nContent(String i18nContent) {
		this.i18nContent = i18nContent;
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
	private CoreConditions endCoreConditions;

	@JsonIgnore
	public CoreConditions getEndCoreConditions() {
		if (null == endCoreConditions) {
			endCoreConditions = CoreConditionManager.getInstance().getCoreConditions(1, endConditionIds);
		}
		return endCoreConditions;
	}

	@Transient
	private CoreConditions openCoreConditions;

	@JsonIgnore
	public CoreConditions getOpenCoreConditions() {
		if (null == openCoreConditions) {
			openCoreConditions = CoreConditionManager.getInstance().getCoreConditions(1, openConditionIds);
		}
		return openCoreConditions;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public int getConsumeGold() {
		return consumeGold;
	}

	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}

	public String[] getOpenConditionIds() {
		return openConditionIds;
	}

	public void setOpenConditionIds(String[] openConditionIds) {
		this.openConditionIds = openConditionIds;
	}

	public String[] getEndConditionIds() {
		return endConditionIds;
	}

	public void setEndConditionIds(String[] endConditionIds) {
		this.endConditionIds = endConditionIds;
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
	
	
}
