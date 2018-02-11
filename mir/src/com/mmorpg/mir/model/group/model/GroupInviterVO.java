package com.mmorpg.mir.model.group.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class GroupInviterVO {
	private long playerId;
	private String name;
	private int role;
	private int level;
	private String server;
	
	public static GroupInviterVO valueOf(Player player) {
		GroupInviterVO vo = new GroupInviterVO();
		vo.playerId = player.getObjectId();
		vo.name = player.getName();
		vo.role = player.getRole();
		vo.level = player.getLevel();
		vo.server = player.getPlayerEnt().getServer();
		return vo;
	}
	
	public long getPlayerId() {
    	return playerId;
    }
	public void setPlayerId(long playerId) {
    	this.playerId = playerId;
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public int getRole() {
    	return role;
    }
	public void setRole(int role) {
    	this.role = role;
    }
	public int getLevel() {
    	return level;
    }
	public void setLevel(int level) {
    	this.level = level;
    }
	public String getServer() {
    	return server;
    }
	public void setServer(String server) {
    	this.server = server;
    }
	
}
