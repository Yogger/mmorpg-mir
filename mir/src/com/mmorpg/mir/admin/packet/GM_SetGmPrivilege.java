package com.mmorpg.mir.admin.packet;

import java.util.HashSet;

public class GM_SetGmPrivilege {
	private String account;
	private String serverId;
	private String op;
	private HashSet<Integer> privileges;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public HashSet<Integer> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(HashSet<Integer> privileges) {
		this.privileges = privileges;
	}

}
