package com.mmorpg.mir.model.country.packet;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.country.packet.vo.CountryNpcDamageVO;

public class SM_CountryNpc_Fight_Rank {
	private Map<Integer, CountryNpcDamageVO> playerDamageRank;
	private Map<Integer, Long> countryDamageRank;
	private long flagMaxHp;
	private byte sign;
	private byte country;

	public static SM_CountryNpc_Fight_Rank valueOf(Map<Integer, CountryNpcDamageVO> nameDamages, Map<Integer, Long> countryDamageRank, long flagMaxHp,
			int sign, int country) {
		SM_CountryNpc_Fight_Rank sm = new SM_CountryNpc_Fight_Rank();
		sm.playerDamageRank = nameDamages;
		sm.flagMaxHp = flagMaxHp;
		sm.countryDamageRank = new HashMap<Integer, Long>(countryDamageRank);
		sm.sign = (byte) sign;
		sm.country = (byte) country;
		return sm;
	}

	public long getFlagMaxHp() {
		return flagMaxHp;
	}

	public void setFlagMaxHp(long flagMaxHp) {
		this.flagMaxHp = flagMaxHp;
	}

	public Map<Integer, CountryNpcDamageVO> getNameDamages() {
		return playerDamageRank;
	}

	public void setNameDamages(Map<Integer, CountryNpcDamageVO> nameDamages) {
		this.playerDamageRank = nameDamages;
	}

	public Map<Integer, CountryNpcDamageVO> getPlayerDamageRank() {
		return playerDamageRank;
	}

	public void setPlayerDamageRank(Map<Integer, CountryNpcDamageVO> playerDamageRank) {
		this.playerDamageRank = playerDamageRank;
	}

	public Map<Integer, Long> getCountryDamageRank() {
		return countryDamageRank;
	}

	public void setCountryDamageRank(Map<Integer, Long> countryDamageRank) {
		this.countryDamageRank = countryDamageRank;
	}

	public byte getSign() {
		return sign;
	}

	public void setSign(byte sign) {
		this.sign = sign;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

}
