package com.mmorpg.mir.model.warship.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.warship.model.Warship;

public class SM_Warship_Status {

	private int supoortCount;
	private int contemptCount;
	private long becomeTime;
	private String kingName;
	private byte countryValue;
	private byte warshipCount;
	private byte currentSelect;
	private byte role;
	
	public static SM_Warship_Status valueOf(Player king, Warship warship, long becomeTime, int supportCount, int contemptCount) {
		SM_Warship_Status sm = new SM_Warship_Status();
		if (king != null) {
			sm.becomeTime = becomeTime;
			sm.kingName = king.getName();
			sm.countryValue = (byte) king.getCountryValue();
			sm.role = (byte) king.getRole();
		}
		sm.supoortCount = supportCount;
		sm.contemptCount = contemptCount;
		sm.warshipCount = (byte) warship.getWarshipCount();
		sm.currentSelect = (byte) warship.getCurrentSelect();
		return sm;
	}

	public int getSupoortCount() {
		return supoortCount;
	}

	public void setSupoortCount(int supoortCount) {
		this.supoortCount = supoortCount;
	}

	public int getContemptCount() {
		return contemptCount;
	}

	public void setContemptCount(int contemptCount) {
		this.contemptCount = contemptCount;
	}

	public long getBecomeTime() {
		return becomeTime;
	}

	public void setBecomeTime(long becomeTime) {
		this.becomeTime = becomeTime;
	}

	public String getKingName() {
		return kingName;
	}

	public void setKingName(String kingName) {
		this.kingName = kingName;
	}

	public byte getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(byte countryValue) {
		this.countryValue = countryValue;
	}

	public byte getWarshipCount() {
		return warshipCount;
	}

	public void setWarshipCount(byte warshipCount) {
		this.warshipCount = warshipCount;
	}

	public byte getCurrentSelect() {
		return currentSelect;
	}

	public void setCurrentSelect(byte currentSelect) {
		this.currentSelect = currentSelect;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

}
