package com.mmorpg.mir.model.item;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditions;

public class UseableItem extends AbstractUseableItem {

	@JsonIgnore
	public CoreConditions getItemConditions(int num) {
		return getResource().getItemConditions(num);
	}

	@JsonIgnore
	public CoreActions getItemActions(int num) {
		return getResource().getItemActions(num);
	}

	@JsonIgnore
	public String getReward() {
		return getResource().getReward();
	}

	@JsonIgnore
	public int getGroup() {
		return getResource().getGroup();
	}

	@JsonIgnore
	public int getCoolDown() {
		return getResource().getCoolDown();
	}
	
}
