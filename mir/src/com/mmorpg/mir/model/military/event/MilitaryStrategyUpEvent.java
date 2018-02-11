package com.mmorpg.mir.model.military.event;

import com.windforce.common.event.event.IEvent;

public class MilitaryStrategyUpEvent implements IEvent{

	private long playerId;
	private int section;
	private int level;
	
	public static MilitaryStrategyUpEvent valueOf(long pid, int section, int level) {
		MilitaryStrategyUpEvent event = new MilitaryStrategyUpEvent();
		event.playerId = pid;
		event.section = section;
		event.level = level;
		return event;
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

	public int getSection() {
    	return section;
    }

	public void setSection(int section) {
    	this.section = section;
    }

	public int getLevel() {
    	return level;
    }

	public void setLevel(int level) {
    	this.level = level;
    }
	
}
