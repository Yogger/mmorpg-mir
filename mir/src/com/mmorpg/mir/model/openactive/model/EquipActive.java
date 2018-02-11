package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import com.windforce.common.utility.New;

public class EquipActive {

	private HashSet<String> rewarded = New.hashSet();

	public static EquipActive valueOf() {
		EquipActive result = new EquipActive();
		result.rewarded = New.hashSet();
		return result;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

}
