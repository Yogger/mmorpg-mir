package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.item.SmeltVO;
import com.mmorpg.mir.model.item.storage.ItemStorage;

public class SM_SmeltEquipment {
	private int type;
	private int code;
	private long addValue;
	private Map<Integer, Object> packUpdate;
	private Map<Integer, SmeltVO> smeltLog;

	public static SM_SmeltEquipment valueOf(int type, ItemStorage storage, int code, long returnTotoalAddValue,
			Map<Integer, SmeltVO> log) {
		SM_SmeltEquipment sm = new SM_SmeltEquipment();
		sm.type = type;
		sm.packUpdate = storage.collectUpdate();
		sm.addValue = returnTotoalAddValue;
		sm.smeltLog = log;
		sm.code = code;
		return sm;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public long getAddValue() {
		return addValue;
	}

	public void setAddValue(long addValue) {
		this.addValue = addValue;
	}

	public Map<Integer, SmeltVO> getSmeltLog() {
		return smeltLog;
	}

	public void setSmeltLog(Map<Integer, SmeltVO> smeltLog) {
		this.smeltLog = smeltLog;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
