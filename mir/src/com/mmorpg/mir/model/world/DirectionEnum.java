package com.mmorpg.mir.model.world;

import com.mmorpg.mir.model.utils.MathUtil;

public enum DirectionEnum {
	// 坐标2相对坐标1的角度 7：↖， 6：←， 5：↙， 4：↓， 3：↘， 2：→，1：↗，0：↑
	// 0：↑
	UP(0, -1, MathUtil.LINE),
	// 1：↗
	RU(1, -1, MathUtil.DIAGONAL),
	// 2：→
	RI(1, 0, MathUtil.LINE),
	// 3：↘
	RD(1, 1, MathUtil.DIAGONAL),
	// 4：↓
	DN(0, 1, MathUtil.LINE),
	// 5：↙
	LD(-1, 1, MathUtil.DIAGONAL),
	// 6：←
	LE(-1, 0, MathUtil.LINE),
	// 7：↖
	LU(-1, -1, MathUtil.DIAGONAL);

	private final int addX;
	private final int addY;
	private final float length;

	public static final DirectionEnum[][] directions = { { LU, LE, LD },
			{ UP, null, DN }, { RU, RI, RD } };

	private DirectionEnum(int addX, int addY, float length) {
		this.addX = addX;
		this.addY = addY;
		this.length = length;
	}

	public static DirectionEnum indexOrdinal(int ordinal) {
		for (DirectionEnum de : values()) {
			if (de.ordinal() == ordinal) {
				return de;
			}
		}
		throw new RuntimeException("not found this ordinal[" + ordinal + "]");
	}

	public float getLength() {
		return length;
	}

	public int getAddX() {
		return addX;
	}

	public int getAddY() {
		return addY;
	}

}
