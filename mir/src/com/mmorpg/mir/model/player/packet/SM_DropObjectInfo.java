package com.mmorpg.mir.model.player.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.DropObject;

public class SM_DropObjectInfo {
	private long objId;
	private short x;
	private short y;
	private String type;
	private String code;
	private int num;
	private boolean isNew;
	private ArrayList<Long> ownership;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public static SM_DropObjectInfo valueOf(DropObject obj) {
		SM_DropObjectInfo result = new SM_DropObjectInfo();
		result.objId = obj.getObjectId();
		result.type = obj.getRewardItem().getRewardType().name();
		result.code = obj.getRewardItem().getCode();
		result.num = obj.getRewardItem().getAmount();
		result.x = (short) obj.getX();
		result.y = (short) obj.getY();
		if (obj.getOwnership() != null) {
			result.ownership = (ArrayList<Long>) obj.getOwnership();
		}
		result.isNew = obj.isNew();
		return result;
	}

	public ArrayList<Long> getOwnership() {
		return ownership;
	}

	public void setOwnership(ArrayList<Long> ownership) {
		this.ownership = ownership;
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

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}
