package com.mmorpg.mir.model.footprint.packet;

public class SM_Add_Footprint {
	private int id;
	private long endTime;
	private int star;

	public static SM_Add_Footprint valueOf(int id, long endTime, int star) {
		SM_Add_Footprint sm = new SM_Add_Footprint();
		sm.id = id;
		sm.endTime = endTime;
		sm.star = star;
		return sm;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

}
