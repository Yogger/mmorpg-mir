package com.mmorpg.mir.model.capturetown.packet;

public class SM_Town_Acc_Feats {

	private int accFeats;
	
	public static SM_Town_Acc_Feats valueOf(int f) {
		SM_Town_Acc_Feats sm = new SM_Town_Acc_Feats();
		sm.accFeats = f;
		return sm;
	}

	public int getAccFeats() {
		return accFeats;
	}

	public void setAccFeats(int accFeats) {
		this.accFeats = accFeats;
	}
	
}
