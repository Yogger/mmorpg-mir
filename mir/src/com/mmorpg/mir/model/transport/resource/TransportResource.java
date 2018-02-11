package com.mmorpg.mir.model.transport.resource;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.world.Position;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class TransportResource {
	/**
	 * 标识 名称 资源路径 所在地图 触发的半径（格子） 所在地图X（格子） 所在地图Y（格子） 传送至目标地图 目标地图X（格子） 目标地图Y（格子）
	 * id res belongMapId radius belongPosX belongPosY targetMapId targetX
	 * targetY
	 */
	@Id
	private int id;
	/** 所在地图 */
	private int belongMapId;
	/** 触发半径 */
	private int radius;
	/** 所在地图X */
	private int belongPosX;

	private int belongPosY;
	
	private int targetMapId;

	private String chooserGroupId;

	private Map<String, Position> destinationPos;
	
	private String[] conditionIds;
	
	@JsonIgnore
	private CoreConditions conditions;
	
	public String[] getConditionIds() {
    	return conditionIds;
    }

	public void setConditionIds(String[] conditionIds) {
    	this.conditionIds = conditionIds;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBelongMapId() {
		return belongMapId;
	}

	public void setBelongMapId(int belongMapId) {
		this.belongMapId = belongMapId;
	}

	public int getBelongPosX() {
		return belongPosX;
	}

	public void setBelongPosX(int belongPosX) {
		this.belongPosX = belongPosX;
	}

	public int getBelongPosY() {
		return belongPosY;
	}

	public void setBelongPosY(int belongPosY) {
		this.belongPosY = belongPosY;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public Map<String, Position> getDestinationPos() {
		return destinationPos;
	}

	public void setDestinationPos(Map<String, Position> destinationPos) {
		this.destinationPos = destinationPos;
	}

	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (conditionIds == null) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionIds);
			}
		}
		return conditions;
	}

	public int getTargetMapId() {
		return targetMapId;
	}

	public void setTargetMapId(int targetMapId) {
		this.targetMapId = targetMapId;
	}

}
