package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class SM_SummonInfo {
	private long playerId;
	private long masterId;
	private String masterName;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private long maxHp;
	private short speed;
	private long currentHp;
	private byte die;
	private byte countryValue;
	private long gangId;
	private short level;

	public SM_SummonInfo() {
	}

	public static SM_SummonInfo valueOf(Summon summon) {
		SM_SummonInfo info = new SM_SummonInfo();
		info.playerId = summon.getObjectId();
		info.masterId = summon.getMaster().getObjectId();
		info.masterName = summon.getMaster().getName();
		info.x = (short) summon.getX();
		info.y = (short) summon.getY();
		info.heading = summon.getHeading();
		info.templateId = summon.getTemplateId();
		info.maxHp = summon.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) summon.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(summon.getLifeStats().getCurrentHp());
		info.die = (byte) (summon.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.countryValue = (byte) summon.getCountryValue();
		if (summon.getMaster().getGang() != null) {
			info.gangId = summon.getMaster().getGang().getId();
		}
		info.level = (short) summon.getLevel();
		return info;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public byte getDie() {
		return die;
	}

	public void setDie(byte die) {
		this.die = die;
	}

	public byte getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(byte countryValue) {
		this.countryValue = countryValue;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

}
