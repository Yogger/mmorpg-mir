package com.mmorpg.mir.model.gang.model;

public class PlayerApplyVO {
	private long gangId;
	private long date;

	public static PlayerApplyVO valueOf(PlayerApply playerApply) {
		PlayerApplyVO apply = new PlayerApplyVO();
		apply.gangId = playerApply.getGangId();
		apply.date = playerApply.getDate();
		return apply;
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
		PlayerApplyVO other = (PlayerApplyVO) obj;
		if (gangId != other.gangId)
			return false;
		return true;
	}

}
