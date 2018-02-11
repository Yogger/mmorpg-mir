package com.mmorpg.mir.model.country.resource;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CountryTankResource {
	@Id
	private String id;

	/** 制造条件 */
	private String[] createConditionIds;
	/** 升级条件 */
	private String[] upgradeConditionIds;
	/** 升级消耗 */
	private String[] upgradeActionIds;
	/** 生成消耗 */
	private String[] createActionIds;
	/** 下一级坦克 */
	private String nextLevelId;
	/** 坦克属性 */
	private Stat[] atts;

	@Transient
	private CoreConditions createConditions;
	@Transient
	private CoreConditions upgradeConditions;

	@Transient
	private CoreActions upgradeActions;

	@Transient
	private CoreActions createActions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getUpgradeConditionIds() {
		return upgradeConditionIds;
	}

	public void setUpgradeConditionIds(String[] upgradeConditionIds) {
		this.upgradeConditionIds = upgradeConditionIds;
	}

	public String[] getUpgradeActionIds() {
		return upgradeActionIds;
	}

	public void setUpgradeActionIds(String[] upgradeActionIds) {
		this.upgradeActionIds = upgradeActionIds;
	}

	public String[] getCreateActionIds() {
		return createActionIds;
	}

	public void setCreateActionIds(String[] createActionIds) {
		this.createActionIds = createActionIds;
	}

	@JsonIgnore
	public CoreConditions getUpgradeConditions() {
		if (upgradeConditions == null) {
			if (ArrayUtils.isEmpty(upgradeConditionIds)) {
				upgradeConditions = new CoreConditions();
			} else {
				upgradeConditions = CoreConditionManager.getInstance().getCoreConditions(1, upgradeConditionIds);
			}
		}
		return upgradeConditions;
	}

	@JsonIgnore
	public void setUpgradeConditions(CoreConditions upgradeConditions) {
		this.upgradeConditions = upgradeConditions;
	}

	@JsonIgnore
	public CoreActions getUpgradeActions() {
		if (upgradeActions == null) {
			if (ArrayUtils.isEmpty(upgradeActionIds)) {
				upgradeActions = new CoreActions();
			} else {
				upgradeActions = CoreActionManager.getInstance().getCoreActions(1, upgradeActionIds);
			}
		}
		return upgradeActions;
	}

	@JsonIgnore
	public void setUpgradeActions(CoreActions upgradeActions) {
		this.upgradeActions = upgradeActions;
	}

	@JsonIgnore
	public CoreActions getCreateActions() {
		if (createActions == null) {
			if (ArrayUtils.isEmpty(createActionIds)) {
				createActions = new CoreActions();
			} else {
				createActions = CoreActionManager.getInstance().getCoreActions(1, createActionIds);
			}
		}
		return createActions;
	}

	@JsonIgnore
	public void setCreateActions(CoreActions createActions) {
		this.createActions = createActions;
	}

	public String getNextLevelId() {
		return nextLevelId;
	}

	public void setNextLevelId(String nextLevelId) {
		this.nextLevelId = nextLevelId;
	}

	public String[] getCreateConditionIds() {
		return createConditionIds;
	}

	public void setCreateConditionIds(String[] createConditionIds) {
		this.createConditionIds = createConditionIds;
	}

	@JsonIgnore
	public CoreConditions getCreateConditions() {
		if (createConditions == null) {
			if (ArrayUtils.isEmpty(createConditionIds)) {
				createConditions = new CoreConditions();
			} else {
				createConditions = CoreConditionManager.getInstance().getCoreConditions(1, createConditionIds);
			}
		}
		return createConditions;
	}

	@JsonIgnore
	public void setCreateConditions(CoreConditions createConditions) {
		this.createConditions = createConditions;
	}

	public Stat[] getAtts() {
		return atts;
	}

	public void setAtts(Stat[] atts) {
		this.atts = atts;
	}

}
