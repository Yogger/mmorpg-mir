package com.mmorpg.mir.model.express.model;

import com.mmorpg.mir.model.gameobjects.Lorry;

public class LorryVO {

	private long objId;
	/** 是否被劫持 */
	private boolean rob;
	/** 是否无敌 */
	private int color;
	/** 最后一次远离时间 */
	private long lastFarawayTime;
	/** 开始时间 */
	private long startTime;
	/** 是否能移动 */
	private boolean canMove;
	/** 镖车资源KEY */
	private String spawnKey;
	/** 当前镖车地图信息 */
	private int mapId;
	private int x;
	private int y;
	private long maxHp;
	private long currentHp;

	public static LorryVO valueOf(Lorry lorry) {
		LorryVO vo = new LorryVO();
		vo.rob = lorry.isRob();
		vo.color = lorry.getColor();
		vo.lastFarawayTime = lorry.getLastFarawayTime();
		vo.startTime = lorry.getStartTime();
		vo.canMove = lorry.canMove();
		vo.spawnKey = lorry.getSpawnKey();
		vo.mapId = lorry.getMapId();
		vo.x = lorry.getX();
		vo.y = lorry.getY();
		vo.objId = lorry.getObjectId();
		vo.maxHp = lorry.getLifeStats().getMaxHp();
		vo.currentHp = lorry.getLifeStats().getCurrentHp();
		return vo;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public boolean isRob() {
		return rob;
	}

	public void setRob(boolean rob) {
		this.rob = rob;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public long getLastFarawayTime() {
		return lastFarawayTime;
	}

	public void setLastFarawayTime(long lastFarawayTime) {
		this.lastFarawayTime = lastFarawayTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
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

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

}
