package com.mmorpg.mir.model.artifact.packet;

public class SM_Artifact_Buff {
	private long buffEndTime;
	private int appLevel;

	public static SM_Artifact_Buff valueOf(long buffEndTime, int appLevel) {
		SM_Artifact_Buff result = new SM_Artifact_Buff();
		result.buffEndTime = buffEndTime;
		result.appLevel = appLevel;
		return result;
	}

	public long getBuffEndTime() {
		return buffEndTime;
	}

	public void setBuffEndTime(long buffEndTime) {
		this.buffEndTime = buffEndTime;
	}

	public int getAppLevel() {
		return appLevel;
	}

	public void setAppLevel(int appLevel) {
		this.appLevel = appLevel;
	}

}
