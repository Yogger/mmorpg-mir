package com.mmorpg.mir.model.investigate.packet;

public class SM_Investigate_Accept {
	private String npc;
	private int count;

	public static SM_Investigate_Accept valueOf(String npc, int count) {
		SM_Investigate_Accept sm = new SM_Investigate_Accept();
		sm.npc = npc;
		sm.count = count;
		return sm;
	}

	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
