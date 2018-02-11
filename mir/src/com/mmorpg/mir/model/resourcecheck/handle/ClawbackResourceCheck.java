package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class ClawbackResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, ChooserGroup> chooserGroupResources;

	@Static
	private Storage<Integer, ClawbackResource> clawbackResourceResources;

	@Override
	public Class<?> getResourceClass() {
		return ClawbackResource.class;
	}

	@Override
	public void check() {
		for (ClawbackResource rs : clawbackResourceResources.getAll()) {
			if (rs.getRewardGroupId() != null) {
				ChooserGroup chooserGroup = chooserGroupResources.get(rs.getRewardGroupId(), false);
				if (chooserGroup == null) {
					throw new RuntimeException(String.format("ClawbackResource id[%s] getRewardGroupId[%s]不存在！ ",
							rs.getEventId(), rs.getRewardGroupId()));
				}
			}
		}
	}
}
