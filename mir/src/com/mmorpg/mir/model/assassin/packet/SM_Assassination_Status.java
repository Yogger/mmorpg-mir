package com.mmorpg.mir.model.assassin.packet;

import com.mmorpg.mir.model.assassin.config.AssassinConfig;
import com.mmorpg.mir.model.gameobjects.Npc;

public class SM_Assassination_Status {

	private int x;
	private int y;
	private long currentHp;
	private long maxHp;
	private int leftRoad;
	
	public static SM_Assassination_Status valueOf(Npc npc) {
		SM_Assassination_Status sm = new SM_Assassination_Status();
		sm.x = npc.getPosition().getX();
		sm.y = npc.getPosition().getY();
		sm.currentHp = npc.getLifeStats().getCurrentHp();
		sm.maxHp = npc.getLifeStats().getMaxHp();
		sm.leftRoad = AssassinConfig.getInstance().getLeftRoad(npc.getPosition().getX(), npc.getPosition().getY());
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

	public int getLeftRoad() {
		return leftRoad;
	}

	public void setLeftRoad(int leftRoad) {
		this.leftRoad = leftRoad;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

}
