package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class SM_LorryInfo {
	private long objId;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private long maxHp;
	private short speed;
	private long currentHp;
	private byte die;
	private boolean rob;
	private long master;
	private byte countryValue;
	private String masterName;
	private short level;

	public SM_LorryInfo() {
	}

	public static SM_LorryInfo valueOf(Lorry lorry) {
		SM_LorryInfo info = new SM_LorryInfo();
		info.objId = lorry.getObjectId();
		info.x = (short) lorry.getX();
		info.y = (short) lorry.getY();
		info.heading = lorry.getHeading();
		info.templateId = lorry.getTemplateId();
		info.maxHp = lorry.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) lorry.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(lorry.getLifeStats().getCurrentHp());
		info.die = (byte) (lorry.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.setRob(lorry.isRob());
		info.master = lorry.getOwner().getObjectId();
		info.countryValue = (byte) lorry.getCountryValue();
		info.masterName = lorry.getOwner().getName();
		info.level = (short) lorry.getLevel();
		return info;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
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

	public boolean isRob() {
		return rob;
	}

	public void setRob(boolean rob) {
		this.rob = rob;
	}

	public long getMaster() {
		return master;
	}

	public void setMaster(long master) {
		this.master = master;
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
