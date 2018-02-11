package com.mmorpg.mir.model.copy.packet;

import com.mmorpg.mir.model.copy.model.CopyHistory;

public class SM_Copy_LadderComplete {

	public static SM_Copy_LadderComplete valueOf(CopyHistory copyHistory) {
		SM_Copy_LadderComplete sm = new SM_Copy_LadderComplete();
		sm.index = copyHistory.getLadderCompleteIndex();
		return sm;
	}

	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
