package com.mmorpg.mir.model.seal.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class SealUpGradeEvent implements IEvent {

	private long playerId;
	private int grade;
	private int level;

	public static SealUpGradeEvent valueOf(Player player) {
		SealUpGradeEvent result = new SealUpGradeEvent();
		result.playerId = player.getObjectId();
		result.grade = player.getSeal().getGrade();
		result.level = player.getSeal().getLevel();
		return result;
	}

	@Override
	public long getOwner() {
		return this.playerId;
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
