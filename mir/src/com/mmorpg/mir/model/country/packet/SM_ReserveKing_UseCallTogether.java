package com.mmorpg.mir.model.country.packet;

public class SM_ReserveKing_UseCallTogether {
	private long nextCallTime;

	public static SM_ReserveKing_UseCallTogether valueOf(long nextCallTime) {
		SM_ReserveKing_UseCallTogether result = new SM_ReserveKing_UseCallTogether();
		result.nextCallTime = nextCallTime;
		return result;
	}

	public long getNextCallTime() {
		return nextCallTime;
	}

	public void setNextCallTime(long nextCallTime) {
		this.nextCallTime = nextCallTime;
	}

}
