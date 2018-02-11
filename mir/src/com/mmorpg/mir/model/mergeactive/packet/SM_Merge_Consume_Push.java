package com.mmorpg.mir.model.mergeactive.packet;

import java.util.ArrayList;

public class SM_Merge_Consume_Push {

	private ArrayList<String> canRecieves;
	
	public static SM_Merge_Consume_Push valueOf(ArrayList<String> canRecieves) {
		SM_Merge_Consume_Push sm = new SM_Merge_Consume_Push();
		sm.canRecieves = canRecieves;
		return sm;
	}

	public ArrayList<String> getCanRecieves() {
		return canRecieves;
	}

	public void setCanRecieves(ArrayList<String> canRecieves) {
		this.canRecieves = canRecieves;
	}

}
