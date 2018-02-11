package com.mmorpg.mir.model.express.model;

import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.spawn.SpawnManager;

public class LorryNavigatorVO {
	private String ownerName;
	private int mapId;
	private int instanceId;
	private int x;
	private int y;
	private int countryValue;
	private boolean beenRob;
	private int color;
	private int destinationMapId;
	
	public static LorryNavigatorVO valueOf(Lorry lorry) {
		LorryNavigatorVO vo = new LorryNavigatorVO();
		vo.mapId = lorry.getMapId();
		vo.x = lorry.getX();
		vo.y = lorry.getY();
		vo.instanceId = lorry.getInstanceId();
		vo.ownerName = lorry.getOwner().getName();
		vo.countryValue = lorry.getCountryValue();
		vo.beenRob = lorry.isRob();
		vo.color = lorry.getColor();
		RouteStep[] rs = SpawnManager.getInstance().getSpawn(lorry.getSpawnKey()).getRouteSteps();
		vo.destinationMapId = rs[rs.length - 1].getMapId(); 
		return vo;
	}
	
	public String getOwnerName() {
    	return ownerName;
    }

	public void setOwnerName(String ownerName) {
    	this.ownerName = ownerName;
    }

	public int getMapId() {
    	return mapId;
    }
	public void setMapId(int mapId) {
    	this.mapId = mapId;
    }
	public int getInstanceId() {
    	return instanceId;
    }
	public void setInstanceId(int instanceId) {
    	this.instanceId = instanceId;
    }
	public int getX() {
    	return x;
    }
	public void setX(int x) {
    	this.x = x;
    }
	public int getY() {
    	return y;
    }
	public void setY(int y) {
    	this.y = y;
    }

	public int getCountryValue() {
    	return countryValue;
    }

	public void setCountryValue(int countryValue) {
    	this.countryValue = countryValue;
    }

	public boolean isBeenRob() {
    	return beenRob;
    }

	public void setBeenRob(boolean beenRob) {
    	this.beenRob = beenRob;
    }

	public int getColor() {
    	return color;
    }

	public void setColor(int color) {
    	this.color = color;
    }

	public int getDestinationMapId() {
		return destinationMapId;
	}

	public void setDestinationMapId(int destinationMapId) {
		this.destinationMapId = destinationMapId;
	}

}
