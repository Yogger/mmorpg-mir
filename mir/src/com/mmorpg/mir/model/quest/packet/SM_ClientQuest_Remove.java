package com.mmorpg.mir.model.quest.packet;

public class SM_ClientQuest_Remove {
	private short id;

	public static SM_ClientQuest_Remove valueOf(short id) {
		SM_ClientQuest_Remove cr = new SM_ClientQuest_Remove();
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
