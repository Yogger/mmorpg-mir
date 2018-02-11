package com.mmorpg.mir.model;

import javax.annotation.PostConstruct;

import com.windforce.common.utility.JsonUtils;

public class ServerConfigValue {
	private int opid;
	private String op;
	private int serverid;
	private String key;
	private String game;
	private int console;
	/** 防沉迷 */
	private int antiAdd;
	/** 老用户判断接口 */
	private String masterAccountUrl;

	private String centerIp;

	private int centerPort;

	private String centerRpcPort;

	private String centerDomain;
	
	private String gmPort;

	/** 服务器版本号 */
	public static String versionMd5 = null;

	private static ServerConfigValue self;

	public static ServerConfigValue getInstance() {
		return self;
	}

	@PostConstruct
	public void init() {
		self = this;
	}

	public int getOpid() {
		return opid;
	}

	public void setOpid(int opid) {
		this.opid = opid;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public int getConsole() {
		return console;
	}

	public void setConsole(int console) {
		this.console = console;
	}

	public int getAntiAdd() {
		return antiAdd;
	}

	public void setAntiAdd(int antiAdd) {
		this.antiAdd = antiAdd;
	}

	public static void main(String[] args) {
		ServerConfigValue v = new ServerConfigValue();
		System.out.println(JsonUtils.object2String(v));
	}

	public String getMasterAccountUrl() {
		return masterAccountUrl;
	}

	public void setMasterAccountUrl(String masterAccountUrl) {
		this.masterAccountUrl = masterAccountUrl;
	}

	public String getCenterIp() {
		return centerIp;
	}

	public void setCenterIp(String centerIp) {
		this.centerIp = centerIp;
	}

	public int getCenterPort() {
		return centerPort;
	}

	public void setCenterPort(int centerPort) {
		this.centerPort = centerPort;
	}

	public String getCenterDomain() {
		return centerDomain;
	}

	public void setCenterDomain(String centerDomain) {
		this.centerDomain = centerDomain;
	}

	public String getCenterRpcPort() {
		return centerRpcPort;
	}

	public void setCenterRpcPort(String centerRpcPort) {
		this.centerRpcPort = centerRpcPort;
	}

	public String getGmPort() {
		return gmPort;
	}

	public void setGmPort(String gmPort) {
		this.gmPort = gmPort;
	}

}
