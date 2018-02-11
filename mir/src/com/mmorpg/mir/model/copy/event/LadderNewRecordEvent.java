package com.mmorpg.mir.model.copy.event;

import com.windforce.common.event.event.IEvent;

public class LadderNewRecordEvent implements IEvent{

	private long playerId;
	private int ladderLayer;
	private int consumeTime;

	public static LadderNewRecordEvent valueOf(long pid, int index, int cost) {
		LadderNewRecordEvent e = new LadderNewRecordEvent();
		e.playerId = pid;
		e.ladderLayer = index;
		e.consumeTime = cost;
		return e;
	}
	@Override
    public long getOwner() {
	    return playerId;
    }

	public long getPlayerId() {
    	return playerId;
    }

	public void setPlayerId(long playerId) {
    	this.playerId = playerId;
    }

	public int getLadderLayer() {
    	return ladderLayer;
    }

	public void setLadderLayer(int ladderLayer) {
    	this.ladderLayer = ladderLayer;
    }

	public int getConsumeTime() {
    	return consumeTime;
    }

	public void setConsumeTime(int consumeTime) {
    	this.consumeTime = consumeTime;
    }

}
