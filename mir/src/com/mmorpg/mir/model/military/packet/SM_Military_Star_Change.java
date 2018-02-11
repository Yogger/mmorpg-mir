package com.mmorpg.mir.model.military.packet;

public class SM_Military_Star_Change {

	private int code;
	private String currentStarId;

	public static SM_Military_Star_Change valueOf(int code, String starId) {
		SM_Military_Star_Change sm = new SM_Military_Star_Change();
		sm.currentStarId = starId;
		sm.code = code;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCurrentStarId() {
		return currentStarId;
	}

	public void setCurrentStarId(String currentStarId) {
		this.currentStarId = currentStarId;
	}

}
