package com.mmorpg.mir.model.quest.packet;

public class SM_Quest_Complete {
	private int count;
	private short id;

	public static SM_Quest_Complete valueOf(short id, int count) {
		SM_Quest_Complete cr = new SM_Quest_Complete();
		cr.id = id;
		cr.count = count;
		return cr;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
