package com.mmorpg.mir.model.temple.resouce;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.temple.manager.TempleManager;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class BrickResource {
	@Id
	private String id;
	/** 随机到的概率 */
	private int weight;
	/** 该砖块是否能被替换 */
	private boolean canChange;
	/** buffId */
	private int skillId;
	/** 奖励的groupId */
	private String chooserGroupId;
	/** 砖块颜色I18N的ID */
	private String colorI18n;

	
	@Transient
	private String bestId; // the field canChange == false --> bestId
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isCanChange() {
		return canChange;
	}

	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public String getColorI18n() {
		return colorI18n;
	}

	public void setColorI18n(String colorI18n) {
		this.colorI18n = colorI18n;
	}

	@JsonIgnore
	public String getBestId() {
		if (bestId == null || bestId.length() == 0) {
			for (BrickResource resource: TempleManager.getInstance().getBrickResources().getAll()) {
				if (!resource.isCanChange()) {
					bestId = resource.id;
				}
			}
		}
		return bestId;
	}
}
