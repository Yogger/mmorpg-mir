package com.mmorpg.mir.model.assassin.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class AssassinRandVO implements Comparable<AssassinRandVO> {
	private long playerId;
	private String name;
	private int randPoints;

	public static AssassinRandVO valueOf(Player player, int points) {
		AssassinRandVO vo = new AssassinRandVO();
		vo.name = player.getName();
		vo.randPoints = points;
		vo.playerId = player.getObjectId();
		return vo;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRandPoints() {
		return randPoints;
	}

	public void setRandPoints(int randPoints) {
		this.randPoints = randPoints;
	}

	@Override
	public int compareTo(AssassinRandVO o) {
		return o.randPoints - randPoints;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
