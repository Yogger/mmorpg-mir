package com.mmorpg.mir.model.country.model.countryact;

public enum CountryFlagQuestType {
	
	/** 防守国旗 */
	DEFENCE(1),
	
	/** 攻击敌国国旗 */
	ATTACK(2),
	
	/** 和同盟国家一起攻击一个国旗 */
	ATTACK_WITH_ALLIANCE(3);
	
	private final int value;
	
	private CountryFlagQuestType(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return value;
	}
}
