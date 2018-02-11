package com.mmorpg.mir.model.world.packet;

public class SM_ChangeChannel {

	private int channelId;

	public static SM_ChangeChannel valueOf(int channelId) {
		SM_ChangeChannel scc = new SM_ChangeChannel();
		scc.channelId = channelId;
		return scc;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

}
