package com.mmorpg.mir.model.welfare.resource;

import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.GiftCollectCondition;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.New;

@Resource
public class GiftCollectResource {

	@Id
	private String id;
	/** 显示为已领取的条件 */
	private String[] conditions;

	@Transient
	private CoreConditions coreConditions;

	@Transient
	private List<Class<?>> events;

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (coreConditions == null) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditions);
		}
		return coreConditions;
	}

	@JsonIgnore
	public List<Class<?>> getEvents() {
		if (events == null) {
			events = New.arrayList();
			for (AbstractCoreCondition condition : getCoreConditions().getConditionList()) {
				if (condition instanceof GiftCollectCondition) {
					GiftCollectCondition c = (GiftCollectCondition) condition;
					for (Class<?> clazz : c.getGiftCollectEvent()) {
						if (!events.contains(clazz)) {
							events.add(clazz);
						}
					}
				}
			}
		}
		return events;
	}

	@JsonIgnore
	public void setEvents(List<Class<?>> events) {
		this.events = events;
	}

	@JsonIgnore
	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditions() {
		return conditions;
	}

	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

}
