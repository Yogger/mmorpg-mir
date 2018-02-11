package com.mmorpg.mir.model.player.resource;

import java.util.Arrays;
import java.util.List;

public enum RPType {
	/** 平民 */
	CIVILIANS(1),
	/** 义士 */
	EAGERGUY(2),
	/** 侠士 */
	CHIVALROUS(3),
	/** 豪侠 */
	GALLANT(4),
	/** 英杰 */
	HERO(5);
	
	public static final List<RPType> allRpType = Arrays.asList(RPType.values());
	
	private final int value;
	
	private RPType(int value) {
		this.value = value;
	}
	
	public RPType valueof(int value) {
		for (RPType r: allRpType) {
			if (r.getValue() == value)
				return r;
		}
		
		throw new IllegalArgumentException("不存在的value");
	}
	public int getValue() {
		return value;
	}
}
