package com.mmorpg.mir.model.countrycopy.packet;

import com.mmorpg.mir.model.countrycopy.model.CountryCopy;
import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Get_Encourage_Info {
	
	private int sign;

	private int nameSize;
	
	private String playerName;
		
	private boolean enroll;
	
	private long enrollStartTime;
	
	private int leftCount;
	
	private long startTime;
	
	private byte role;
	
	private byte promotionId;

	public static SM_Get_Encourage_Info valueOf(int sign, CountryCopy copy, Player player) {
		SM_Get_Encourage_Info sm = new SM_Get_Encourage_Info();
		if (!copy.getEncourageList().isEmpty()) {
			Player firstMan = copy.getEncourageList().get(1);
			sm.promotionId = (byte) firstMan.getPromotion().getStage();
			sm.role = (byte) firstMan.getRole();
			sm.playerName = firstMan.getName();
		}
		sm.nameSize = copy.getEncourageList().size();
		sm.enroll = copy.hasEnrolled(player);
		sm.enrollStartTime = copy.getEnrollTime();
		sm.startTime = copy.getStartTime();
		sm.leftCount = player.getCountryCopyInfo().getLeftCount();
		sm.sign = sign;
		return sm;
	}

	public boolean isEnroll() {
		return enroll;
	}

	public void setEnroll(boolean enroll) {
		this.enroll = enroll;
	}

	public long getEnrollStartTime() {
		return enrollStartTime;
	}

	public void setEnrollStartTime(long enrollStartTime) {
		this.enrollStartTime = enrollStartTime;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(byte promotionId) {
		this.promotionId = promotionId;
	}

	public int getNameSize() {
		return nameSize;
	}

	public void setNameSize(int nameSize) {
		this.nameSize = nameSize;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
