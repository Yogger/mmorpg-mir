package com.mmorpg.mir.model.chat.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ChannelResource {
	@Id
	private int id;
	private String[] conditions;
	private String[] filterConditions;
	private String[] acts;
	private ChannelType scope;
	private long cd;

	@Transient
	private CoreConditions coreFilterConditions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getConditions() {
		return conditions;
	}

	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

	public String[] getActs() {
		return acts;
	}

	public void setActs(String[] acts) {
		this.acts = acts;
	}

	public ChannelType getScope() {
		return scope;
	}

	public void setScope(ChannelType scope) {
		this.scope = scope;
	}

	public long getCd() {
		return cd;
	}

	public void setCd(long cd) {
		this.cd = cd;
	}

	public String[] getFilterConditions() {
		return filterConditions;
	}

	public void setFilterConditions(String[] filterConditions) {
		this.filterConditions = filterConditions;
	}

	@JsonIgnore
	public CoreConditions getCoreFilterConditions() {
		if (coreFilterConditions == null) {
			if (filterConditions == null) {
				coreFilterConditions = new CoreConditions();
			} else {
				coreFilterConditions = CoreConditionManager.getInstance().getCoreConditions(1, filterConditions);
			}
		}
		return coreFilterConditions;
	}

	@JsonIgnore
	public void setCoreFilterConditions(CoreConditions coreFilterConditions) {
		this.coreFilterConditions = coreFilterConditions;
	}

}
