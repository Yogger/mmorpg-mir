package com.mmorpg.mir.model.mail.packet;

import java.util.HashSet;

public class CM_DeleteMail {
	private HashSet<Integer> indexs;
	private boolean isDirect;

	public HashSet<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(HashSet<Integer> indexs) {
		this.indexs = indexs;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

}
