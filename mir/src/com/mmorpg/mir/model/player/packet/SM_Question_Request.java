package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SM_Question_Request {
	private int questionid;
	private long overTime;
	private PlayerSimpleInfo simpleInfo;
	private int promotionStage;

	public int getQuestionid() {
		return questionid;
	}

	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}

	public long getOverTime() {
		return overTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public static SM_Question_Request valueOf(int questionid, Player player, long overTime) {
		SM_Question_Request req = new SM_Question_Request();
		req.questionid = questionid;
		req.simpleInfo = player.createSimple();
		req.promotionStage = player.getPromotion().getStage();
		req.overTime = overTime;
		return req;
	}

	public PlayerSimpleInfo getSimpleInfo() {
		return simpleInfo;
	}

	public void setSimpleInfo(PlayerSimpleInfo simpleInfo) {
		this.simpleInfo = simpleInfo;
	}

	public int getPromotionStage() {
		return promotionStage;
	}

	public void setPromotionStage(int promotionStage) {
		this.promotionStage = promotionStage;
	}

}
