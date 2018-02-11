package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonFirstPayResource {
	public static final String ACTIVE_NAME = "ACTIVE_NAME";

	@Id
	private String id;
	@Index(name = ACTIVE_NAME)
	private String activeName;
	/** 首充领取的条件 */
	private String[] conditonIds;
	/** 奖励的id */
	private String choosergroupId;
	/** 档次 */
	private int level;
	/** 开启首充的条件 */
	private String[] openConditionIds;
	/** 结束后领取条件 */
	private String[] endConditionIds;
	/** I18n的标题 */
	private String i18nTitle;
	/** */
	private String i18nContent;
	/** 合服清理 */
	private boolean mergeRemove;

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

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
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

	public String getChoosergroupId() {
		return choosergroupId;
	}

	public void setChoosergroupId(String choosergroupId) {
		this.choosergroupId = choosergroupId;
	}

	public boolean isMergeRemove() {
		return mergeRemove;
	}

	public void setMergeRemove(boolean mergeRemove) {
		this.mergeRemove = mergeRemove;
	}

}
