package com.mmorpg.mir.model.gangofwar.packet.vo;

public class BossStatus {
	private long hp;
	private byte god;

	public static BossStatus valueOf(long hp, boolean god) {
		BossStatus bs = new BossStatus();
		bs.hp = hp;
		bs.god = god ? (byte) 1 : 0;
		return bs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + god;
		result = prime * result + (int) (hp ^ (hp >>> 32));
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
		BossStatus other = (BossStatus) obj;
		if (god != other.god)
			return false;
		if (hp != other.hp)
			return false;
		return true;
	}

	public long getHp() {
		return hp;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}

	public byte getGod() {
		return god;
	}

	public void setGod(byte god) {
		this.god = god;
	}

}
