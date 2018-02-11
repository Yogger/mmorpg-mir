package com.mmorpg.mir.model.contact.model;

public class EnemyVO {

	private long playerId;
	private int enmity;
	private int slaughtCount;

	public static EnemyVO valueOf(long pid, Integer enmity, Integer count) {
		EnemyVO vo = new EnemyVO();
		vo.playerId = pid;
		vo.enmity = (enmity == null? 0: enmity);
		vo.slaughtCount = (count == null? 0: count);
		return vo;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getEnmity() {
		return enmity;
	}

	public void setEnmity(int enmity) {
		this.enmity = enmity;
	}

	public int getSlaughtCount() {
		return slaughtCount;
	}

	public void setSlaughtCount(int slaughtCount) {
		this.slaughtCount = slaughtCount;
	}

}
