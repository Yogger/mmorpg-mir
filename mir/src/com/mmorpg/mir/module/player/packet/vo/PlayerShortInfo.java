package com.mmorpg.mir.module.player.packet.vo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class PlayerShortInfo {
	@Protobuf
	private long playerId;
	@Protobuf
	private String account;
	@Protobuf
	private int serverId;
	@Protobuf
	private String name;
	@Protobuf
	private int role;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

}
