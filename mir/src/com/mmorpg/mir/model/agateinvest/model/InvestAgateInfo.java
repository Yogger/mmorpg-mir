package com.mmorpg.mir.model.agateinvest.model;

public class InvestAgateInfo {
	private String playerName;
	private int countryId;
	/** 领取的资源id */
	private String resouceId;
	/** 投资类型 */
	private int type;

	public static InvestAgateInfo valueOf(String playerName, int countryId, int type, String resourceId) {
		InvestAgateInfo result = new InvestAgateInfo();
		result.playerName = playerName;
		result.countryId = countryId;
		result.resouceId = resourceId;
		result.type = type;
		return result;
	}

	public static InvestAgateInfo valueOf(String playerName, int countryId, int type) {
		InvestAgateInfo result = new InvestAgateInfo();
		result.playerName = playerName;
		result.countryId = countryId;
		result.type = type;
		result.resouceId = "";
		return result;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getResouceId() {
		return resouceId;
	}

	public void setResouceId(String resouceId) {
		this.resouceId = resouceId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
