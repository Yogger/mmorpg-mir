package com.mmorpg.mir.admin.packet;

import java.util.Collection;

public class SGM_GetBlockList {
	private Collection<String> result;

	public Collection<String> getResult() {
		return result;
	}

	public void setResult(Collection<String> result) {
		this.result = result;
	}

}
