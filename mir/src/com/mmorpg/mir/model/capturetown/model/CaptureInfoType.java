package com.mmorpg.mir.model.capturetown.model;

public enum CaptureInfoType {
	/** 被攻击,敌人失败了 **/
	BEENATTACKED_FAIL(1),
	/** 被攻击,但是没有失去城池 **/
	BEENATTACKED_SUCC_NOT_LOST_TOWN(2),
	/** 被攻击成功,但失去城池,被落到全民城池 **/
	BEENATTACKED_SUCC_LOST_TOWN(3),
	/** 被攻击成功,但获得了对方的城池 **/
	BEENATTACKED_SUCC_ACQ_TOWN(4),
	
	/** 攻击成功,但是没有获得城池 **/
	ATTACK_SUCC_NOT_ACQ_TOWN(5),
	/** 攻击成功,获得对方城池 **/
	ATTACK_SUCC_ACQ_TOWN(6),
	/** 攻击失败 **/
	ATTACK_FAIL(7),
	;
	
	private final int value;
	
	private CaptureInfoType(int v) {
		this.value = v;
	}
	
	public final int getValue() {
		return value;
	}
	
}
