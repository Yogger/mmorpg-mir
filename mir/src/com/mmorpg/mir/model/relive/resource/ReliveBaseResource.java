package com.mmorpg.mir.model.relive.resource;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.world.WorldType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ReliveBaseResource {
	@Id
	private int reliveId;

	private WorldType type;

	private int allowBuyLife;

	private String chooserGroupId;

	private Map<String, RelivePosition> relivePositions;

	public int getReliveId() {
		return reliveId;
	}

	public void setReliveId(int reliveId) {
		this.reliveId = reliveId;
	}

	public WorldType getType() {
		return type;
	}

	public void setType(WorldType type) {
		this.type = type;
	}

	public int getAllowBuyLife() {
		return allowBuyLife;
	}

	public void setAllowBuyLife(int allowBuyLife) {
		this.allowBuyLife = allowBuyLife;
	}

	@JsonIgnore
	public boolean isAllowBuyLife() {
		return allowBuyLife > 0;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public Map<String, RelivePosition> getRelivePositions() {
		return relivePositions;
	}

	public void setRelivePositions(Map<String, RelivePosition> relivePositions) {
		this.relivePositions = relivePositions;
	}

}
