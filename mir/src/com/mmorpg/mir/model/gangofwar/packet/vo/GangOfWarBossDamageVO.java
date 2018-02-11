package com.mmorpg.mir.model.gangofwar.packet.vo;

import com.mmorpg.mir.model.gang.model.Gang;

public class GangOfWarBossDamageVO implements Comparable<GangOfWarBossDamageVO> {
	private String gangName;
	private String server;
	private long damage;

	public static GangOfWarBossDamageVO valueOf(Gang gang, long damage) {
		GangOfWarBossDamageVO vo = new GangOfWarBossDamageVO();
		vo.damage = damage;
		vo.gangName = gang.getName();
		vo.server = gang.getServer();
		return vo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gangName == null) ? 0 : gangName.hashCode());
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
		GangOfWarBossDamageVO other = (GangOfWarBossDamageVO) obj;
		if (gangName == null) {
			if (other.gangName != null)
				return false;
		} else if (!gangName.equals(other.gangName))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		return true;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	@Override
	public int compareTo(GangOfWarBossDamageVO o) {
		return (int) (o.getDamage() - damage);
	}

}
