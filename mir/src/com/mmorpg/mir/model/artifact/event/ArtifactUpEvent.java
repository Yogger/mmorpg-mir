package com.mmorpg.mir.model.artifact.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class ArtifactUpEvent implements IEvent {

	private long playerId;
	private int grade;
	private int level;

	public static ArtifactUpEvent valueOf(Player player) {
		ArtifactUpEvent event = new ArtifactUpEvent();
		event.playerId = player.getObjectId();
		event.grade = player.getArtifact().getLevel();
		event.level = player.getArtifact().getRank();
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
