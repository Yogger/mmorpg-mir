package com.mmorpg.mir.model.item.model;

public enum EquipmentGroup {
	/** 玩家身上所有装备组 **/
	ALL(0),
	/** 玩家身上攻击部位的装备组 **/
	ATTACK_GROUP(1),
	/** 玩家身上防御部位的装备组 **/
	DEFEND_GROUP(2);
	
	private final int value;
	
	private EquipmentGroup(int v) {
		this.value = v;
	}
	
	public final int getValue() {
		return value;
	}
	
}
