package com.mmorpg.mir.model.world.packet;

public class SM_Get_MapChannel {
	private int maxChannels;

	public static SM_Get_MapChannel valueOf(int maxChannels) {
		SM_Get_MapChannel sgm = new SM_Get_MapChannel();
		sgm.maxChannels = maxChannels;
		return sgm;
	}

	public int getMaxChannels() {
		return maxChannels;
	}

	public void setMaxChannels(int maxChannels) {
		this.maxChannels = maxChannels;
	}

}
