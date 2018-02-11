package com.mmorpg.mir.model.horse.packet;

public class SM_Not_Finish_Acitve {
	private long time;

	public static SM_Not_Finish_Acitve valueOf(long time) {
		SM_Not_Finish_Acitve result = new SM_Not_Finish_Acitve();
		result.time = time;
		return result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
