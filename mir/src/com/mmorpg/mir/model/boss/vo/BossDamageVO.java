package com.mmorpg.mir.model.boss.vo;

import com.mmorpg.mir.model.gameobjects.Player;

public class BossDamageVO {
	private String name;
	private String server;
	private long damage;
	private int rank;
	private byte country;

	public static BossDamageVO valueOf(Player player, String name, String server, long damage, int rank) {
		BossDamageVO vo = new BossDamageVO();
		vo.name = name;
		vo.damage = damage;
		vo.rank = rank;
		vo.country = (byte) player.getCountryValue();
		return vo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
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
		BossDamageVO other = (BossDamageVO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

}
