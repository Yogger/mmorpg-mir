package com.mmorpg.mir.model.investigate.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class InvestigateResource {
	@Id
	private String id;
	/** 完成条件 */
	private String[] completeCondtionIds;
	/** 奖励chooser */
	private String chooserGroupId;
	/** 颜色 */
	private int color;
	/** 刺探情报颜色I18N ID */
	private String colorI18n;

	@Transient
	private CoreConditions compleConditions;

	@JsonIgnore
	public CoreConditions getCompleteConditions() {
		if (compleConditions == null) {
			compleConditions = CoreConditionManager.getInstance().getCoreConditions(1, completeCondtionIds);
		}
		return compleConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getCompleteCondtionIds() {
		return completeCondtionIds;
	}

	public void setCompleteCondtionIds(String[] completeCondtionIds) {
		this.completeCondtionIds = completeCondtionIds;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getColorI18n() {
		return colorI18n;
	}

	public void setColorI18n(String colorI18n) {
		this.colorI18n = colorI18n;
	}

}
