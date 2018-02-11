package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.CountryQuest;
import com.mmorpg.mir.model.country.model.Court;

public class SM_Country_QuestStatus {
	private long templeEndTime;
	private long expressEndTime;
	private boolean officialSalary;
	private boolean civilSalary;
	private boolean haveTraitor;
	
	private int templeLeft;
	private int expressLeft;

	public static SM_Country_QuestStatus valueOf(CountryQuest countryQuest, Court court, boolean haveTraitor) {
		SM_Country_QuestStatus sm = new SM_Country_QuestStatus();
		sm.setExpressEndTime(countryQuest.getExpressEndTime());
		sm.setTempleEndTime(countryQuest.getTempleEndTime());
		sm.officialSalary = court.isOfficialSalary();
		sm.civilSalary = court.isCivilSalary();
		sm.haveTraitor = haveTraitor;
		sm.templeLeft = countryQuest.getTodayTempleLeftCount();
		sm.expressLeft = countryQuest.getTodayExpressLeftCount();
		return sm;
	}

	public long getTempleEndTime() {
		return templeEndTime;
	}

	public void setTempleEndTime(long templeEndTime) {
		this.templeEndTime = templeEndTime;
	}

	public long getExpressEndTime() {
		return expressEndTime;
	}

	public void setExpressEndTime(long expressEndTime) {
		this.expressEndTime = expressEndTime;
	}

	public boolean isOfficialSalary() {
		return officialSalary;
	}

	public void setOfficialSalary(boolean officialSalary) {
		this.officialSalary = officialSalary;
	}

	public boolean isCivilSalary() {
		return civilSalary;
	}

	public void setCivilSalary(boolean civilSalary) {
		this.civilSalary = civilSalary;
	}

	public boolean isHaveTraitor() {
		return haveTraitor;
	}

	public void setHaveTraitor(boolean haveTraitor) {
		this.haveTraitor = haveTraitor;
	}

	public int getTempleLeft() {
		return templeLeft;
	}

	public void setTempleLeft(int templeLeft) {
		this.templeLeft = templeLeft;
	}

	public int getExpressLeft() {
		return expressLeft;
	}

	public void setExpressLeft(int expressLeft) {
		this.expressLeft = expressLeft;
	}

}
