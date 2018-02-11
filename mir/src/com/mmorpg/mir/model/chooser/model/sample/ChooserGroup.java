package com.mmorpg.mir.model.chooser.model.sample;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ChooserGroup {
	@Id
	private String chooserGroupId;

	private String[] valueChoosers;

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public String[] getValueChoosers() {
		return valueChoosers;
	}

	public void setValueChoosers(String[] valueChoosers) {
		this.valueChoosers = valueChoosers;
	}

}
