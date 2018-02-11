package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;

public class SM_PlayerDie {
	private long objId;
	private int skillId;
	private long lastId;
	private String lastName;
	private byte attackerCountry;
	private long deadTime;
	private int buyCount;
	private boolean inBossRange;
	private boolean buyRelive;
	
	public static SM_PlayerDie valueOf(
			Creature creature, int skillId, long lastId, String name, long deadTime, int buyCount, int countryValue, boolean buyRelive) {
		SM_PlayerDie result = new SM_PlayerDie();
		result.objId = creature.getObjectId();
		result.skillId = skillId;
		result.lastId = lastId;
		result.lastName = name;
		result.deadTime = deadTime;
		result.buyCount = buyCount;
		result.attackerCountry = (byte) countryValue;
		result.inBossRange = PlayerReliveManager.getInstance().isInBossRange(creature);
		result.buyRelive = buyRelive;
		return result;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public long getLastId() {
		return lastId;
	}

	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}
	
	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public int getBuyCount() {
    	return buyCount;
    }

	public void setBuyCount(int buyCount) {
    	this.buyCount = buyCount;
    }

	public byte getAttackerCountry() {
		return attackerCountry;
	}

	public void setAttackerCountry(byte attackerCountry) {
		this.attackerCountry = attackerCountry;
	}

	public boolean isInBossRange() {
		return inBossRange;
	}

	public void setInBossRange(boolean inBossRange) {
		this.inBossRange = inBossRange;
	}

	public boolean isBuyRelive() {
		return buyRelive;
	}

	public void setBuyRelive(boolean buyRelive) {
		this.buyRelive = buyRelive;
	}

}
