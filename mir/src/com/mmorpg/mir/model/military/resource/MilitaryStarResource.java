package com.mmorpg.mir.model.military.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MilitaryStarResource {

	public static final String MILIARY_STAR_BUFF_GROUP = "MILITARYSTAR";
	
	@Id
	private String id;
	private String nextId;
	private String previousId;
	private String[] starUpCondition;
	private String[] keepStarDailyAct;
	private int starSkillId;

	@Transient
	private CoreConditions upCondition;
	
	@Transient
	private CoreActions dailyActions;
	
	@JsonIgnore
	public CoreConditions getUpConditions() {
		if (upCondition == null) {
			upCondition = CoreConditionManager.getInstance().getCoreConditions(1, starUpCondition);
		}
		return upCondition;
	}
	
	@JsonIgnore
	public CoreActions getDailyActions(int days) {
		if (days > 1) {
			return CoreActionManager.getInstance().getCoreActions(days, keepStarDailyAct);
		}
		if (dailyActions == null) {
			if (keepStarDailyAct == null) {
				return null;
			} else {
				dailyActions = CoreActionManager.getInstance().getCoreActions(1, keepStarDailyAct);
			}
		}
		return dailyActions;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public String[] getStarUpCondition() {
		return starUpCondition;
	}

	public void setStarUpCondition(String[] starUpCondition) {
		this.starUpCondition = starUpCondition;
	}

	public String[] getKeepStarDailyAct() {
		return keepStarDailyAct;
	}

	public void setKeepStarDailyAct(String[] keepStarDailyAct) {
		this.keepStarDailyAct = keepStarDailyAct;
	}

	public int getStarSkillId() {
		return starSkillId;
	}

	public void setStarSkillId(int starSkillId) {
		this.starSkillId = starSkillId;
	}

	public String getPreviousId() {
    	return previousId;
    }

	public void setPreviousId(String previousId) {
    	this.previousId = previousId;
    }

}
