package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Supervisor;

public class SM_SupervisorInfo {
	private long id;
	private short x;
	private short y;
	private byte heading;
	private short templateId;

	public SM_SupervisorInfo() {
	}

	public static SM_SupervisorInfo valueOf(Supervisor supervisor) {
		SM_SupervisorInfo info = new SM_SupervisorInfo();
		info.id = supervisor.getObjectId();
		info.x = (short) supervisor.getX();
		info.y = (short) supervisor.getY();
		info.heading = supervisor.getHeading();
		info.templateId = supervisor.getTemplateId();
		return info;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

}
