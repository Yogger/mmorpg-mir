package com.mmorpg.mir.model.country.packet;

import java.util.Map;

import com.mmorpg.mir.model.country.packet.vo.CountryNpcDamageVO;

public class SM_Diplomacy_Damage_Rank {

	private Map<Integer, CountryNpcDamageVO> rankMap;
	private long killTime;
	private byte hitCountry;

	public static SM_Diplomacy_Damage_Rank valueOf(Map<Integer, CountryNpcDamageVO> ranks, long time, int countryValue) {
		SM_Diplomacy_Damage_Rank sm = new SM_Diplomacy_Damage_Rank();
		sm.rankMap = ranks;
		sm.killTime = time;
		sm.hitCountry = (byte) countryValue;
		return sm;
	}
	
	public Map<Integer, CountryNpcDamageVO> getRankMap() {
		return rankMap;
	}

	public void setRankMap(Map<Integer, CountryNpcDamageVO> rankMap) {
		this.rankMap = rankMap;
	}

	public long getKillTime() {
		return killTime;
	}

	public void setKillTime(long killTime) {
		this.killTime = killTime;
	}

	public byte getHitCountry() {
		return hitCountry;
	}

	public void setHitCountry(byte hitCountry) {
		this.hitCountry = hitCountry;
	}

}
