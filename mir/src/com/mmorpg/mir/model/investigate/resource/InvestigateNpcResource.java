package com.mmorpg.mir.model.investigate.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class InvestigateNpcResource {
	@Id
	private String id;
	/** 接取条件 */
	private String[] acceptCondtionIds;

	private String[] itemAcceptConditionIds;

	/** 接取的情报chooser */
	private String chooserGroupId;
	/** NPC所属的国家 */
	private int belongCountry;

	@Transient
	private CoreConditions acceptConditions;

	@Transient
	private CoreConditions itemAcceptConditions;

	@JsonIgnore
	public CoreConditions getAcceptConditions() {
		if (acceptConditions == null) {
			acceptConditions = CoreConditionManager.getInstance().getCoreConditions(1, acceptCondtionIds);
		}
		return acceptConditions;
	}

	@JsonIgnore
	public CoreConditions getItemAccepConditions() {
		if (itemAcceptConditions == null) {
			itemAcceptConditions = CoreConditionManager.getInstance().getCoreConditions(1, itemAcceptConditionIds);
		}
		return itemAcceptConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getAcceptCondtionIds() {
		return acceptCondtionIds;
	}

	public void setAcceptCondtionIds(String[] acceptCondtionIds) {
		this.acceptCondtionIds = acceptCondtionIds;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public void setAcceptConditions(CoreConditions acceptConditions) {
		this.acceptConditions = acceptConditions;
	}

	public int getBelongCountry() {
		return belongCountry;
	}

	public void setBelongCountry(int belongCountry) {
		this.belongCountry = belongCountry;
	}

	public String[] getItemAcceptConditionIds() {
		return itemAcceptConditionIds;
	}

	public void setItemAcceptConditionIds(String[] itemAcceptConditionIds) {
		this.itemAcceptConditionIds = itemAcceptConditionIds;
	}

}
