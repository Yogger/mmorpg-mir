package com.mmorpg.mir.model.temple.model;

import java.util.concurrent.Future;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.model.Temple;
import com.mmorpg.mir.model.temple.manager.TempleManager;
import com.mmorpg.mir.model.temple.resouce.BrickResource;

public class Brick {
	/** 来源太庙 */
	private Temple fromTemple;
	/** id */
	private String id;
	private long playerId;
	private long startTime;
	/** 超时计数器 */
	private Future<?> future;
	/** 最后一次换砖的时间 */
	private long lastChangeTime;

	public static Brick valueOf(long playerId, Temple temple, String id) {
		Brick bk = new Brick();
		bk.playerId = playerId;
		bk.fromTemple = temple;
		bk.startTime = System.currentTimeMillis();
		bk.id = id;
		return bk;
	}

	@JsonIgnore
	public BrickResource getResource() {
		return TempleManager.getInstance().getBrickResources().get(id, true);
	}

	@JsonIgnore
	public boolean changeTimeCD(int cd) {
		if ((lastChangeTime + cd) > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void changeId(String id) {
		this.id = id;
		lastChangeTime = System.currentTimeMillis();
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@JsonIgnore
	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}

	@JsonIgnore
	public Temple getFromTemple() {
		return fromTemple;
	}

	public void setFromTemple(Temple fromTemple) {
		this.fromTemple = fromTemple;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

}
