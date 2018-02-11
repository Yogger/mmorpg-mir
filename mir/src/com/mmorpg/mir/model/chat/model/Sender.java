package com.mmorpg.mir.model.chat.model;

public class Sender {
	private long id;
	private String name;
	private int vipLevel;
	private byte country;
	private String official;
	private byte kingOfking;
	private byte gm;
	private String server;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public String getOfficial() {
		return official;
	}

	public void setOfficial(String official) {
		this.official = official;
	}

	public byte getKingOfking() {
		return kingOfking;
	}

	public void setKingOfking(byte kingOfking) {
		this.kingOfking = kingOfking;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public byte getGm() {
		return gm;
	}

	public void setGm(byte gm) {
		this.gm = gm;
	}

}
