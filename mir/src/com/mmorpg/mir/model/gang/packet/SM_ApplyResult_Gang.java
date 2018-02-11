package com.mmorpg.mir.model.gang.packet;

public class SM_ApplyResult_Gang {
	private String code;
	private long gangId;
	private byte result;
	private String server;
	private String gangName;
	private int type;  //1 表示处理邀请返回的结果, 0表示处理申请返回的结果.....
	private String applyPlayerName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public String getServer() {
    	return server;
    }

	public void setServer(String server) {
    	this.server = server;
    }

	public String getGangName() {
    	return gangName;
    }

	public void setGangName(String gangName) {
    	this.gangName = gangName;
    }

	public int getType() {
    	return type;
    }

	public void setType(int type) {
    	this.type = type;
    }

	public String getApplyPlayerName() {
    	return applyPlayerName;
    }

	public void setApplyPlayerName(String applyPlayerName) {
    	this.applyPlayerName = applyPlayerName;
    }

}
