package com.mmorpg.mir.model.country.resource;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CountryFlagResource {
	@Id
	private String id;
	/** 升级消耗 */
	private String[] actions;
	/** 下一级 */
	private String nextLevelId;
	/** 国旗提供给玩家的属性 */
	private Stat[] playerStats;

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

	public Stat[] getPlayerStats() {
		return playerStats;
	}

	public void setPlayerStats(Stat[] playerStats) {
		this.playerStats = playerStats;
	}

}
