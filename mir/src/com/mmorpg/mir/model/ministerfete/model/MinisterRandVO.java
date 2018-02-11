package com.mmorpg.mir.model.ministerfete.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class MinisterRandVO implements Comparable<MinisterRandVO> {
	private long playerId;
	private String name;
	private int randPoints;

	public static MinisterRandVO valueOf(Player player, int points) {
		MinisterRandVO vo = new MinisterRandVO();
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
	public int compareTo(MinisterRandVO o) {
		return o.randPoints - randPoints;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
