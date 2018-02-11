package com.mmorpg.mir.model.lifegrid.packet;

public class SM_LifeGrid_Convert {
	private String id;

	private int point;

	public static SM_LifeGrid_Convert valueOf(String id, int point) {
		SM_LifeGrid_Convert result = new SM_LifeGrid_Convert();
		result.id = id;
		result.point = point;
		return result;
	}

	public String getId() {
		return id;
	}

	public int getPoint() {
		return point;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
