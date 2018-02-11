package com.mmorpg.mir.model.lifegrid.packet;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;

public class SM_LifeGrid_Devour {
	private int code;

	private int fromType;
	private int toType;
	private HashMap<Integer, Object> fromUpdate;
	private HashMap<Integer, Object> toUpdate;

	public static SM_LifeGrid_Devour valueOf(LifeStorageType fromType, LifeStorageType toType,
			Map<Integer, Object> fromUpdate, Map<Integer, Object> toUpdate) {
		SM_LifeGrid_Devour result = new SM_LifeGrid_Devour();
		result.fromType = fromType.getType();
		result.toType = toType.getType();
		result.fromUpdate = new HashMap<Integer, Object>(fromUpdate);
		result.toUpdate = new HashMap<Integer, Object>(toUpdate);
		return result;
	}

	public static SM_LifeGrid_Devour valueOfSameSource(LifeStorageType fromType, LifeStorageType toType,
			Map<Integer, Object> fromUpdate) {
		SM_LifeGrid_Devour result = new SM_LifeGrid_Devour();
		result.fromUpdate = new HashMap<Integer, Object>(fromUpdate);
		result.fromType = fromType.getType();
		result.toType = toType.getType();
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public HashMap<Integer, Object> getFromUpdate() {
		return fromUpdate;
	}

	public void setFromUpdate(HashMap<Integer, Object> fromUpdate) {
		this.fromUpdate = fromUpdate;
	}

	public HashMap<Integer, Object> getToUpdate() {
		return toUpdate;
	}

	public void setToUpdate(HashMap<Integer, Object> toUpdate) {
		this.toUpdate = toUpdate;
	}

	public int getFromType() {
		return fromType;
	}

	public void setFromType(int fromType) {
		this.fromType = fromType;
	}

	public int getToType() {
		return toType;
	}

	public void setToType(int toType) {
		this.toType = toType;
	}

}
