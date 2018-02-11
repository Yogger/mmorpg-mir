package com.mmorpg.mir.model.lifegrid.packet;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;

public class SM_LifeGrid_ExchangePack {

	private int code;

	private int fromType;

	private int toType;

	private HashMap<Integer, Object> fromStorageUpdate;

	private HashMap<Integer, Object> toStorageUpdate;

	public static SM_LifeGrid_ExchangePack valueOfSameType(LifeStorageType fromType, LifeStorageType toType,
			Map<Integer, Object> fromStorageUpdate) {
		SM_LifeGrid_ExchangePack result = new SM_LifeGrid_ExchangePack();
		result.fromType = fromType.getType();
		result.toType = toType.getType();
		result.fromStorageUpdate = new HashMap<Integer, Object>(fromStorageUpdate);
		return result;
	}

	public static SM_LifeGrid_ExchangePack valueOf(LifeStorageType fromType, LifeStorageType toType,
			Map<Integer, Object> fromStorageUpdate, Map<Integer, Object> toStorageUpdate) {
		SM_LifeGrid_ExchangePack result = new SM_LifeGrid_ExchangePack();
		result.fromType = fromType.getType();
		result.toType = toType.getType();
		result.fromStorageUpdate = new HashMap<Integer, Object>(fromStorageUpdate);
		result.toStorageUpdate = new HashMap<Integer, Object>(toStorageUpdate);
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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

	public HashMap<Integer, Object> getFromStorageUpdate() {
		return fromStorageUpdate;
	}

	public void setFromStorageUpdate(HashMap<Integer, Object> fromStorageUpdate) {
		this.fromStorageUpdate = fromStorageUpdate;
	}

	public HashMap<Integer, Object> getToStorageUpdate() {
		return toStorageUpdate;
	}

	public void setToStorageUpdate(HashMap<Integer, Object> toStorageUpdate) {
		this.toStorageUpdate = toStorageUpdate;
	}

}
