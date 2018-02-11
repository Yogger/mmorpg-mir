package com.mmorpg.mir.model.world;

public final class MapGrid {
	private byte state = 0;
	private static final byte BLOCK = 1;
	private static final byte SAFE = 1 << 1;

	public void openBlock() {
		state |= BLOCK;
	}

	public void openSafe() {
		state |= SAFE;
	}

	public boolean isBlock() {
		return (state & BLOCK) > 0;
	}

	public boolean isSafe() {
		return (state & SAFE) > 0;
	}

}
