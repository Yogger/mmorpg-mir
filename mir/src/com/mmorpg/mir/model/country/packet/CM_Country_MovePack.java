package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Country_MovePack extends CM_System_Sign {
	private byte type;
	private int fromIndex;
	private int toIndex;
	private boolean inPack;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}

	public boolean isInPack() {
		return inPack;
	}

	public void setInPack(boolean inPack) {
		this.inPack = inPack;
	}

}
