package com.mmorpg.mir.model.ministerfete.packet;

public class SM_Enter_Minister {
	private long nextSkillTime;

	public static SM_Enter_Minister valueOf(long t) {
		SM_Enter_Minister sm = new SM_Enter_Minister();
		sm.nextSkillTime = t;
		return sm;
	}
	
	public long getNextSkillTime() {
		return nextSkillTime;
	}

	public void setNextSkillTime(long nextSkillTime) {
		this.nextSkillTime = nextSkillTime;
	}
	
}
