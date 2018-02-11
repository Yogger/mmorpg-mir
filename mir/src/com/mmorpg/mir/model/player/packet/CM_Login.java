package com.mmorpg.mir.model.player.packet;

public class CM_Login {
	private String accountName;
	private String op;
	private String server;
	private String sign;
	private int isAdult;
	private int loginType;
	private int opVipLevel;
	private String time;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getIsAdult() {
		return isAdult;
	}

	public void setIsAdult(int isAdult) {
		this.isAdult = isAdult;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public int getOpVipLevel() {
		return opVipLevel;
	}

	public void setOpVipLevel(int opVipLevel) {
		this.opVipLevel = opVipLevel;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
