package com.mmorpg.mir.model.countrycopy.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;

public class FirstEncourageVO {

	private String name;
	
	private byte promotionId;
	
	private byte role;
	
	public static FirstEncourageVO valueOf(Player player) {
		FirstEncourageVO vo = new FirstEncourageVO();
		vo.name = player.getName();
		vo.promotionId = (byte) player.getPromotion().getStage();
		vo.role = (byte) player.getRole();
		return vo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(byte promotionId) {
		this.promotionId = promotionId;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}
	
}
