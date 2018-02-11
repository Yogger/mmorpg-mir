package com.mmorpg.mir.model.ministerfete.packet;

import com.mmorpg.mir.model.gameobjects.Npc;

public class SM_Minister_Status {

	private int x;
	private int y;
	private long currentHp;
	private long maxHp;
	
	public static SM_Minister_Status valueOf(Npc npc) {
		SM_Minister_Status sm = new SM_Minister_Status();
		sm.x = npc.getPosition().getX();
		sm.y = npc.getPosition().getY();
		sm.currentHp = npc.getLifeStats().getCurrentHp();
		sm.maxHp = npc.getLifeStats().getMaxHp();
		return sm;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

}
