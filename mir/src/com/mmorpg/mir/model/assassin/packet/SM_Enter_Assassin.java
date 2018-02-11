package com.mmorpg.mir.model.assassin.packet;

public class SM_Enter_Assassin {
	private long nextSkillTime;

	public static SM_Enter_Assassin valueOf(long t) {
		SM_Enter_Assassin sm = new SM_Enter_Assassin();
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
