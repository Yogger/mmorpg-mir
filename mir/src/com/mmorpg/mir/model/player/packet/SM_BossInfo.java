package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.model.AbnormalVO;

public class SM_BossInfo {
	private long playerId;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private long maxHp;
	private short speed;
	private long currentHp;
	private byte die;
	private AbnormalVO abnormalVo;
	private short level;

	public SM_BossInfo() {
	}

	public static SM_BossInfo valueOf(Boss boss) {
		SM_BossInfo info = new SM_BossInfo();
		info.playerId = boss.getObjectId();
		info.x = (short) boss.getX();
		info.y = (short) boss.getY();
		info.heading = boss.getHeading();
		info.templateId = boss.getTemplateId();
		info.maxHp = boss.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) boss.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(boss.getLifeStats().getCurrentHp());
		info.die = (byte) (boss.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.abnormalVo = new AbnormalVO(boss.getEffectController());
		info.level = (short) boss.getLevel();
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
