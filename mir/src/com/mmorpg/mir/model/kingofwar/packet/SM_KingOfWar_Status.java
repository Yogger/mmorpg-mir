package com.mmorpg.mir.model.kingofwar.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.kingofwar.model.KingOfWarInfo;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class SM_KingOfWar_Status {

	private boolean warring;
	private PlayerSimpleInfo kingOfKing;
	private ArrayList<PlayerSimpleInfo> kings;
	private long becomeKingTime;
	private long nextStarttime;

	public static SM_KingOfWar_Status valueOf(boolean warring, PlayerSimpleInfo kingOfKing, long becomeKingTime,
			long nextStarttime, KingOfWarInfo kingOfWarInfo) {
		SM_KingOfWar_Status sm = new SM_KingOfWar_Status();
		sm.setWarring(warring);
		sm.kingOfKing = kingOfKing;
		sm.becomeKingTime = becomeKingTime;
		sm.nextStarttime = nextStarttime;
		sm.kings = new ArrayList<PlayerSimpleInfo>();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			Official ko = country.getCourt().getKing();
			if (ko != null) {
				if (kingOfKing == null || kingOfKing.getPlayerId() != ko.getPlayerId()) {
					sm.kings.add(PlayerManager.getInstance().getPlayer(ko.getPlayerId()).createSimple());
				}
			}
		}
		return sm;
	}

	public PlayerSimpleInfo getKingOfKing() {
		return kingOfKing;
	}

	public void setKingOfKing(PlayerSimpleInfo kingOfKing) {
		this.kingOfKing = kingOfKing;
	}

	public long getBecomeKingTime() {
		return becomeKingTime;
	}

	public void setBecomeKingTime(long becomeKingTime) {
		this.becomeKingTime = becomeKingTime;
	}

	public long getNextStarttime() {
		return nextStarttime;
	}

	public void setNextStarttime(long nextStarttime) {
		this.nextStarttime = nextStarttime;
	}

	public boolean isWarring() {
		return warring;
	}

	public void setWarring(boolean warring) {
		this.warring = warring;
	}

	public ArrayList<PlayerSimpleInfo> getKings() {
		return kings;
	}

	public void setKings(ArrayList<PlayerSimpleInfo> kings) {
		this.kings = kings;
	}

}
