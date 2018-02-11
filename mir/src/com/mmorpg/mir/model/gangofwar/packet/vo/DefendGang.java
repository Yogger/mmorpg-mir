package com.mmorpg.mir.model.gangofwar.packet.vo;

public class DefendGang {
	private long defendEndTime;
	private long defendGangId;
	private String defendGangName;

	public static DefendGang valueOf(long defendEndTime, long defendGangId, String defendGangName) {
		DefendGang defendGang = new DefendGang();
		defendGang.defendEndTime = defendEndTime;
		defendGang.defendGangId = defendGangId;
		defendGang.defendGangName = defendGangName;
		return defendGang;
	}

	public long getDefendEndTime() {
		return defendEndTime;
	}

	public void setDefendEndTime(long defendEndTime) {
		this.defendEndTime = defendEndTime;
	}

	public long getDefendGangId() {
		return defendGangId;
	}

	public void setDefendGangId(long defendGangId) {
		this.defendGangId = defendGangId;
	}

	public String getDefendGangName() {
		return defendGangName;
	}

	public void setDefendGangName(String defendGangName) {
		this.defendGangName = defendGangName;
	}

}
