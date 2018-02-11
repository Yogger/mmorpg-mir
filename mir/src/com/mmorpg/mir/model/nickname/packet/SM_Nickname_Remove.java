package com.mmorpg.mir.model.nickname.packet;

import java.util.ArrayList;
import java.util.Arrays;

public class SM_Nickname_Remove {
	private ArrayList<Integer> removeActives;

	public static SM_Nickname_Remove valueOf(Integer... ids) {
		SM_Nickname_Remove sm = new SM_Nickname_Remove();
		sm.removeActives = new ArrayList<Integer>(Arrays.asList(ids));
		return sm;
	}

	public ArrayList<Integer> getRemoveActives() {
		return removeActives;
	}

	public void setRemoveActives(ArrayList<Integer> removeActives) {
		this.removeActives = removeActives;
	}
	
}
