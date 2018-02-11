package com.mmorpg.mir.model.object.followpolicy;

public interface IFollowPolicy {

	public boolean outWarning(int tx, int ty);

	public boolean tooFarFromHome(int tx, int ty);
}
