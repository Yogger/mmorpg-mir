package com.mmorpg.mir.model.country.model;

public class KingGurad {
	private long playerId;
	private int index;

	public static KingGurad valueOf(long playerId, int index) {
		KingGurad kg = new KingGurad();
		kg.playerId = playerId;
		kg.index = index;
		return kg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (playerId ^ (playerId >>> 32));
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
		KingGurad other = (KingGurad) obj;
		if (playerId != other.playerId)
			return false;
		return true;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
