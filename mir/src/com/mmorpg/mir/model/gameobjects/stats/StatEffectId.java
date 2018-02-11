package com.mmorpg.mir.model.gameobjects.stats;

public class StatEffectId implements Comparable<StatEffectId> {
	private String id;
	private StatEffectType type;
	/** 不统计到战斗力中 */
	private boolean noBattleScore;

	public static StatEffectId valueOf(String id, StatEffectType type) {
		return valueOf(id, type, false);
	}

	public static StatEffectId valueOf(long id, StatEffectType type) {
		return valueOf(String.valueOf(id), type, false);
	}

	public static StatEffectId valueOf(String id, StatEffectType type, boolean noBattleScore) {
		StatEffectId sId = new StatEffectId();
		sId.id = id;
		sId.type = type;
		sId.noBattleScore = noBattleScore;
		return sId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		StatEffectId other = (StatEffectId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StatEffectType getType() {
		return type;
	}

	public void setType(StatEffectType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return id + "-" + type.name();
	}

	public boolean isNoBattleScore() {
		return noBattleScore;
	}

	public void setNoBattleScore(boolean noBattleScore) {
		this.noBattleScore = noBattleScore;
	}

	@Override
	public int compareTo(StatEffectId o) {
		String source = this.id + this.type.toString();
		String target = o.id + o.type.toString();
		return source.compareTo(target);
	}

}
