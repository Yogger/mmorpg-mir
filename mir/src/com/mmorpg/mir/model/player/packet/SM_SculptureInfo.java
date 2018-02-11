package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Sculpture;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;

public class SM_SculptureInfo {
	private long objId;
	private short templateId;
	private short x;
	private short y;
	private byte heading;
	private String name;
	private byte role;
	private byte country;
	/** 是否是皇帝 1-是，0-不是 */
	private byte kingOfking;

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

	public static SM_SculptureInfo valueOf(Sculpture obj) {
		SM_SculptureInfo result = new SM_SculptureInfo();
		result.objId = obj.getObjectId();
		result.templateId = obj.getTemplateId();
		result.x = (short) obj.getX();
		result.y = (short) obj.getY();
		result.heading = obj.getHeading();
		result.name = obj.getPlayerName();
		result.country = (byte) obj.getCountry();
		result.role = (byte) obj.getRole();
		result.kingOfking = (byte) (KingOfWarManager.getInstance().isKingOfKing(obj.getPlayerId())? 1: 0); 
		return result;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public byte getKingOfking() {
		return kingOfking;
	}

	public void setKingOfking(byte kingOfking) {
		this.kingOfking = kingOfking;
	}

}
