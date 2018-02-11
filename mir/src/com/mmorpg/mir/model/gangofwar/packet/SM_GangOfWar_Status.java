package com.mmorpg.mir.model.gangofwar.packet;

import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.model.GangSimpleVO;

public class SM_GangOfWar_Status {

	private boolean warring;
	private GangSimpleVO defendGang;
	private long becomeKingTime;
	private int control;
	private long nextOpenTime;

	public static SM_GangOfWar_Status valueOf(boolean warring, Gang gang, Country country, long nextStartTime) {
		SM_GangOfWar_Status sm = new SM_GangOfWar_Status();
		sm.warring = warring;
		if (gang != null) {
			sm.defendGang = gang.creatSimpleVO();
		}
		sm.becomeKingTime = country.getCourt().getBecomeKingTime();
		sm.control = country.getCourt().getControl();
		sm.nextOpenTime = nextStartTime;
		return sm;
	}

	public boolean isWarring() {
		return warring;
	}

	public void setWarring(boolean warring) {
		this.warring = warring;
	}

	public GangSimpleVO getDefendGang() {
		return defendGang;
	}

	public void setDefendGang(GangSimpleVO defendGang) {
		this.defendGang = defendGang;
	}

	public long getBecomeKingTime() {
		return becomeKingTime;
	}

	public void setBecomeKingTime(long becomeKingTime) {
		this.becomeKingTime = becomeKingTime;
	}

	public int getControl() {
		return control;
	}

	public void setControl(int control) {
		this.control = control;
	}

	public long getNextOpenTime() {
		return nextOpenTime;
	}

	public void setNextOpenTime(long nextOpenTime) {
		this.nextOpenTime = nextOpenTime;
	}

}