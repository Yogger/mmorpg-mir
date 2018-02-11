package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class SM_BigBrotherInfo {
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
	@Deprecated
	private boolean ride;
	@Deprecated
	private int grade;
	@Deprecated
	private int appearanceId;
	private boolean create;
	private String createdRoleServer;

	public SM_BigBrotherInfo() {
	}

	public static SM_BigBrotherInfo valueOf(BigBrother bigBrother) {
		SM_BigBrotherInfo info = new SM_BigBrotherInfo();
		info.playerId = bigBrother.getObjectId();
		info.masterId = bigBrother.getMaster().getObjectId();
		info.masterName = bigBrother.getMaster().getName();
		info.x = (short) bigBrother.getX();
		info.y = (short) bigBrother.getY();
		info.heading = bigBrother.getHeading();
		info.templateId = bigBrother.getTemplateId();
		info.maxHp = bigBrother.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) bigBrother.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(bigBrother.getLifeStats().getCurrentHp());
		info.die = (byte) (bigBrother.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.countryValue = (byte) bigBrother.getCountryValue();
		info.ride = false;
		info.grade = 0;
		info.appearanceId = 0;
		if (bigBrother.getMaster().getGang() != null) {
			info.gangId = bigBrother.getMaster().getGang().getId();
		}
		info.level = (short) bigBrother.getLevel();
		info.create = bigBrother.isNew();
		info.createdRoleServer = bigBrother.getMaster().getPlayerEnt().getServer();
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

	public void setMaxHp(int maxHp) {
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

	public boolean isRide() {
		return ride;
	}

	public void setRide(boolean ride) {
		this.ride = ride;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getAppearanceId() {
		return appearanceId;
	}

	public void setAppearanceId(int appearanceId) {
		this.appearanceId = appearanceId;
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public String getCreatedRoleServer() {
		return createdRoleServer;
	}

	public void setCreatedRoleServer(String createdRoleServer) {
		this.createdRoleServer = createdRoleServer;
	}

}
