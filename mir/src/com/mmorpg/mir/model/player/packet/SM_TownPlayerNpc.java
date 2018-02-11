package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.TownPlayerNpc;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class SM_TownPlayerNpc {
	private long playerId;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private long maxHp;
	private short speed;
	private long currentHp;
	private long maxBarrier;
	private long currentBarrier;
	private byte die;
	private short level;
	private SM_PlayerInfo playerInfo;

	public SM_TownPlayerNpc() {
	}

	public static SM_TownPlayerNpc valueOf(TownPlayerNpc townPlayerNpc) {
		SM_TownPlayerNpc info = new SM_TownPlayerNpc();
		info.playerId = townPlayerNpc.getObjectId();
		info.x = (short) townPlayerNpc.getX();
		info.y = (short) townPlayerNpc.getY();
		info.heading = townPlayerNpc.getHeading();
		info.templateId = townPlayerNpc.getTemplateId();
		info.maxHp = townPlayerNpc.getGameStats().getCurrentStat(StatEnum.MAXHP);
		info.speed = (short) townPlayerNpc.getGameStats().getCurrentStat(StatEnum.SPEED);
		info.setCurrentHp(townPlayerNpc.getLifeStats().getCurrentHp());
		info.die = (byte) (townPlayerNpc.getLifeStats().isAlreadyDead() ? 1 : 0);
		info.level = (short) townPlayerNpc.getLevel();
		info.playerInfo = townPlayerNpc.getPlayerInfo();
		info.maxBarrier = townPlayerNpc.getGameStats().getCurrentStat(StatEnum.BARRIER);
		info.currentBarrier = townPlayerNpc.getLifeStats().getCurrentBarrier();
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

	public SM_PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(SM_PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}

	public long getMaxBarrier() {
		return maxBarrier;
	}

	public void setMaxBarrier(long maxBarrier) {
		this.maxBarrier = maxBarrier;
	}

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

}
