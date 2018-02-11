package com.mmorpg.mir.model.gang.model;

public class PlayerApply {
	private long gangId;
	private long date;

	public static PlayerApply valueOf(long gangId) {
		PlayerApply apply = new PlayerApply();
		apply.gangId = gangId;
		apply.date = System.currentTimeMillis();
		return apply;
	}

	public PlayerApplyVO createVO() {
		PlayerApplyVO vo = PlayerApplyVO.valueOf(this);
		return vo;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gangId ^ (gangId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerApply other = (PlayerApply) obj;
		if (gangId != other.gangId)
			return false;
		return true;
	}

}
