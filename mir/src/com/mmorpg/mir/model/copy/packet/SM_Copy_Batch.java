package com.mmorpg.mir.model.copy.packet;

import java.util.ArrayList;

public class SM_Copy_Batch {
	private ArrayList<String> batchIds;

	public static SM_Copy_Batch valueOf(ArrayList<String> ids) {
		SM_Copy_Batch sm = new SM_Copy_Batch();
		sm.batchIds = ids;
		return sm;
	}

	public ArrayList<String> getBatchIds() {
		return batchIds;
	}

	public void setBatchIds(ArrayList<String> batchIds) {
		this.batchIds = batchIds;
	}
}
