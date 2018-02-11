package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.model.AbnormalVO;

public class SM_CountryNpcInfo {
	private long playerId;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private long maxHp;
	private short speed;
	private long currentHp;
	private byte die;
	private byte country;
	private AbnormalVO abnormalVo;
	private short level;

	public SM_CountryNpcInfo() {
	}

	public static SM_CountryNpcInfo valueOf(CountryNpc countryNpc) {
		SM_CountryNpcInfo info = new SM_CountryNpcInfo();
		info.playerId = countryNpc.getObjectId();
		info.x = (short) countryNpc.getX();
		info.y = (short) countryNpc.getY();
		info.heading = countryNpc.getHeading();
		info.templateId = countryNpc.getTemplateId();
		info.maxHp = countryNpc.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) countryNpc.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(countryNpc.getLifeStats().getCurrentHp());
		info.die = (byte) (countryNpc.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.country = (byte) (countryNpc.getCountryValue());
		info.abnormalVo = new AbnormalVO(countryNpc.getEffectController());
		info.setLevel((short) countryNpc.getLevel());
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

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public AbnormalVO getAbnormalVo() {
		return abnormalVo;
	}

	public void setAbnormalVo(AbnormalVO abnormalVo) {
		this.abnormalVo = abnormalVo;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

}
