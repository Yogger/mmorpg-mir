package com.mmorpg.mir.admin.packet;

public class SGM_Identify_Treasure_Close {
	private String activeName;
	
	private int code;

	public static SGM_Identify_Treasure_Close valueOf(String activeName){
		SGM_Identify_Treasure_Close sgm = new SGM_Identify_Treasure_Close();
		sgm.activeName = activeName;
		return sgm;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
}
