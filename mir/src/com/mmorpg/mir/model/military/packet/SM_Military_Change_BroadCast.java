package com.mmorpg.mir.model.military.packet;

public class SM_Military_Change_BroadCast {
	private long targetId;
	private int rank;

	public static SM_Military_Change_BroadCast valueOf(long objId, int rank) {
		SM_Military_Change_BroadCast sm = new SM_Military_Change_BroadCast();
		sm.rank = rank;
		sm.targetId = objId;
		return sm;
	}
	
	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
