package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.StatusNpc;

public class SM_StatusNpcInfo {
	private long playerId;
	private short x;
	private short y;
	private byte heading;
	private short templateId;
	private int status;

	public SM_StatusNpcInfo() {
	}

	public static SM_StatusNpcInfo valueOf(StatusNpc statusNpc) {
		SM_StatusNpcInfo info = new SM_StatusNpcInfo();
		info.playerId = statusNpc.getObjectId();
		info.x = (short) statusNpc.getX();
		info.y = (short) statusNpc.getY();
		info.heading = statusNpc.getHeading();
		info.templateId = statusNpc.getTemplateId();
		info.status = statusNpc.getStatus();
		return info;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
