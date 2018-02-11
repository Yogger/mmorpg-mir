package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class SM_MonsterInfo {
	private long playerId;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private long maxHp;
	private short speed;
	private long currentHp;
	private byte die;
	private short level;

	public SM_MonsterInfo() {
	}

	public static SM_MonsterInfo valueOf(Monster monster) {
		SM_MonsterInfo info = new SM_MonsterInfo();
		info.playerId = monster.getObjectId();
		info.x = (short) monster.getX();
		info.y = (short) monster.getY();
		info.heading = monster.getHeading();
		info.templateId = monster.getTemplateId();
		info.maxHp = monster.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) monster.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(monster.getLifeStats().getCurrentHp());
		info.die = (byte) (monster.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.level = (short) monster.getLevel();
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

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

}
