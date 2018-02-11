package com.mmorpg.mir.model.transport.model;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditions;

/**
 * 飞鞋缓存
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-29
 * 
 */
public class PlayerChatTransport {
	private int id;
	private int mapId;
	private int x;
	private int y;
	private long createTime;
	private int instanceId;
	private CoreConditions conditions;

	public static PlayerChatTransport valueOf(int id, int mapId, int x, int y, int instanceId) {
		PlayerChatTransport playerTransport = new PlayerChatTransport();
		playerTransport.id = id;
		playerTransport.mapId = mapId;
		playerTransport.x = x;
		playerTransport.y = y;
		playerTransport.createTime = System.currentTimeMillis();
		playerTransport.instanceId = instanceId;
		return playerTransport;
	}

	public void addConditions(AbstractCoreCondition condition) {
		if (conditions == null) {
			conditions = new CoreConditions();
		}
		conditions.addCondition(condition);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public CoreConditions getConditions() {
		return conditions;
	}

	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

}
