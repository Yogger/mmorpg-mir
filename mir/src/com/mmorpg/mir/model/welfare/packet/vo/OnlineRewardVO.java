package com.mmorpg.mir.model.welfare.packet.vo;

public class OnlineRewardVO {
	
	private int index;
	private String rewadId;

	public static OnlineRewardVO valueOf(int index, String rewadId) {
		OnlineRewardVO vo = new OnlineRewardVO();
		vo.setIndex(index);
		vo.setRewadId(rewadId);
		return vo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getRewadId() {
		return rewadId;
	}

	public void setRewadId(String rewadId) {
		this.rewadId = rewadId;
	}

}
