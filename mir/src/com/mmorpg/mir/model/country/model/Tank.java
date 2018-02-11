package com.mmorpg.mir.model.country.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.model.vo.TankVO;

public class Tank {
	private int id;
	private String resourceId;
	private long owner;
	private int index;

	public static Tank valueOf(int id, String resourceId, int index) {
		Tank tank = new Tank();
		tank.id = id;
		tank.resourceId = resourceId;
		tank.index = index;
		return tank;
	}

	@JsonIgnore
	public boolean hasOwner() {
		return owner > 0;
	}

	@JsonIgnore
	public void callback() {
		owner = 0;
	}

	@JsonIgnore
	public void take(long playerId) {
		owner = playerId;
	}

	public TankVO createVO() {
		TankVO tankVO = TankVO.valueOf(this);
		return tankVO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
