package com.mmorpg.mir.model.quest.packet;

public class SM_ClientQuest_Add {
	private short id;

	public static SM_ClientQuest_Add valueOf(short id) {
		SM_ClientQuest_Add cr = new SM_ClientQuest_Add();
		cr.id = id;
		return cr;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}


}
