package com.mmorpg.mir.model.country.resource;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CountryFactoryResource {
	@Id
	private String id;
	/** 等级 */
	private int level;
	/** 升级消耗 */
	private String[] actions;
	/** 下一级 */
	private String nextLevelId;
	/** 车库大小(存放战车数量) */
	private int garageSize;

	@Transient
	private CoreActions coreActions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public String getNextLevelId() {
		return nextLevelId;
	}

	public void setNextLevelId(String nextLevelId) {
		this.nextLevelId = nextLevelId;
	}

	@JsonIgnore
	public CoreActions getCoreActions() {
		if (coreActions == null) {
			if (ArrayUtils.isEmpty(actions)) {
				coreActions = new CoreActions();
			} else {
				coreActions = CoreActionManager.getInstance().getCoreActions(1, actions);
			}
		}
		return coreActions;
	}

	@JsonIgnore
	public void setCoreActions(CoreActions coreActions) {
		this.coreActions = coreActions;
	}

	public int getGarageSize() {
		return garageSize;
	}

	public void setGarageSize(int garageSize) {
		this.garageSize = garageSize;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
