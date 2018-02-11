package com.mmorpg.mir.model.welfare.packet.vo;

public class BossGiftVO implements Comparable<BossGiftVO>{

	private long playerId;
	private String name;
	private int giftAmount;
	
	public static BossGiftVO valueOf(long pid, String n, int gift) {
		BossGiftVO vo = new BossGiftVO();
		vo.playerId = pid;
		vo.name = n;
		vo.giftAmount = gift;
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

	public int getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(int giftAmount) {
		this.giftAmount = giftAmount;
	}

	@Override
	public int compareTo(BossGiftVO o) {
		return o.getGiftAmount() - this.giftAmount;
	}

}
