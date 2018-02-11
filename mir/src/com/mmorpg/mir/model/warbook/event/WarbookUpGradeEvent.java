package com.mmorpg.mir.model.warbook.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class WarbookUpGradeEvent implements IEvent {

	private long owner;
	private int grade;
	private int level;

	public static WarbookUpGradeEvent valueOf(Player player) {
		WarbookUpGradeEvent result = new WarbookUpGradeEvent();
		result.owner = player.getObjectId();
		result.grade = player.getWarBook().getGrade();
		result.level = player.getWarBook().getLevel();
		return result;
	}

	@Override
	public long getOwner() {
		return this.owner;
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

	public void setOwner(long owner) {
		this.owner = owner;
	}

}
