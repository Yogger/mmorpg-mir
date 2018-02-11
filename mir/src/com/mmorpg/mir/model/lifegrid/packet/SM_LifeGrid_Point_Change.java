package com.mmorpg.mir.model.lifegrid.packet;

public class SM_LifeGrid_Point_Change {
	private int point;

	public static SM_LifeGrid_Point_Change valueOf(int point) {
		SM_LifeGrid_Point_Change result = new SM_LifeGrid_Point_Change();
		result.point = point;
		return result;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
