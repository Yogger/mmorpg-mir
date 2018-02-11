package com.mmorpg.mir.admin.packet;

public class GM_Recharge {
	private String account;
	private String serverId;
	private String op;

	private float rmb;
	private int money;
	private String orderId;
	private long time;

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

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getRmb() {
		return rmb;
	}

	public void setRmb(float rmb) {
		this.rmb = rmb;
	}

}
