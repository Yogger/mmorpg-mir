package com.mmorpg.mir.model.player.packet;

public class CM_CreatePlayer {
	private String account;
	private String op;
	private String server;
	private String sign;
	private String playerName;
	private int role;
	private int isAdult;
	private int loginType;
	private int country;
	private String source;
	private int noframe;
	private String time;
	private int opVipLevel;
	private String userRefer;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getNoframe() {
		return noframe;
	}

	public void setNoframe(int noframe) {
		this.noframe = noframe;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getOpVipLevel() {
		return opVipLevel;
	}

	public void setOpVipLevel(int opVipLevel) {
		this.opVipLevel = opVipLevel;
	}

	public String getUserRefer() {
		return userRefer;
	}

	public void setUserRefer(String userRefer) {
		this.userRefer = userRefer;
	}

}
