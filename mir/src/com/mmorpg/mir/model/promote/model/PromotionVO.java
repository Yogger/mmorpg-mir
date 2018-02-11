package com.mmorpg.mir.model.promote.model;

public class PromotionVO {

	private int stage;
	
	public static PromotionVO valueOf(int s) {
		PromotionVO vo = new PromotionVO();
		vo.stage = s;
		return vo;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
	
}
