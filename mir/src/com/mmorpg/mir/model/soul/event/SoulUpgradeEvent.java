package com.mmorpg.mir.model.soul.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class SoulUpgradeEvent implements IEvent {

	private long playerId;
	private int grade;
	private int level;

	public static SoulUpgradeEvent valueOf(Player player) {
		SoulUpgradeEvent e = new SoulUpgradeEvent();
		e.playerId = player.getObjectId();
		e.grade = player.getSoul().getLevel();
		e.level = player.getSoul().getRank();
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

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
