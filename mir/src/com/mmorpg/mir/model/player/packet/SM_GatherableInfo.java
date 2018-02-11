package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Gatherable;

public class SM_GatherableInfo {
	private long objId;
	private short templateId;
	private short x;
	private short y;
	private byte heading;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
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

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public static SM_GatherableInfo valueOf(Gatherable obj) {
		SM_GatherableInfo result = new SM_GatherableInfo();
		result.objId = obj.getObjectId();
		result.templateId = obj.getTemplateId();
		result.x = (short) obj.getX();
		result.y = (short) obj.getY();
		result.heading = obj.getHeading();
		return result;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}
}
