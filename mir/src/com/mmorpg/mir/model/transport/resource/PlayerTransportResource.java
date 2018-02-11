package com.mmorpg.mir.model.transport.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.world.Position;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.RandomUtils;

@Resource
public class PlayerTransportResource {
	@Id
	private Integer id;

	private String[] conditionId;

	private String[] actionId;

	private int targetMapId;

	private int targetX;

	private int targetY;

	private Position[] randPos;

	private boolean freeForVip;

	private int instance;

	@JsonIgnore
	public Position selectPosition() {
		if (randPos != null && randPos.length >= 1) {
			return randPos[RandomUtils.nextInt(randPos.length)];
		}
		return new Position(targetX, targetY);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getTargetMapId() {
		return targetMapId;
	}

	public void setTargetMapId(int targetMapId) {
		this.targetMapId = targetMapId;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public String[] getConditionId() {
		return conditionId;
	}

	public void setConditionId(String[] conditionId) {
		this.conditionId = conditionId;
	}

	public String[] getActionId() {
		return actionId;
	}

	public void setActionId(String[] actionId) {
		this.actionId = actionId;
	}

	public boolean isFreeForVip() {
		return freeForVip;
	}

	public void setFreeForVip(boolean freeForVip) {
		this.freeForVip = freeForVip;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public Position[] getRandPos() {
		return randPos;
	}

	public void setRandPos(Position[] randPos) {
		this.randPos = randPos;
	}

}
