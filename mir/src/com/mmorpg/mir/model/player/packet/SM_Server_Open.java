package com.mmorpg.mir.model.player.packet;

import java.util.Date;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.serverstate.ServerState;

public class SM_Server_Open {
	private long openTime;
	private long nextKingOfWarStartTime;
	private long nextGangOfWarStartTime;
	private long nextCountryFlagStartTime;
	private long nextDiplomacyStartTime;
	private long mergeTime;

	public static SM_Server_Open valueOf(Date openDate, Player player) {
		SM_Server_Open sm = new SM_Server_Open();
		sm.openTime = openDate.getTime();
		sm.nextKingOfWarStartTime = KingOfWarManager.getInstance().getNextKingOfWarTime();
		sm.nextGangOfWarStartTime = GangOfWarManager.getInstance().getGangOfWars(player)
				.getNextStartTime(player.getCountry());
		sm.nextCountryFlagStartTime = CountryManager.getInstance().getOpenServerFlagSpawnTime(openDate);
		sm.nextDiplomacyStartTime = CountryManager.getInstance().getOpenServerDiplomacySpawnTime(openDate);
		sm.mergeTime = ServerState.getInstance().getMergeTime() == null ? 0 : ServerState.getInstance().getMergeTime()
				.getTime();
		return sm;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public long getNextKingOfWarStartTime() {
		return nextKingOfWarStartTime;
	}

	public void setNextKingOfWarStartTime(long nextKingOfWarStartTime) {
		this.nextKingOfWarStartTime = nextKingOfWarStartTime;
	}

	public long getNextGangOfWarStartTime() {
		return nextGangOfWarStartTime;
	}

	public void setNextGangOfWarStartTime(long nextGangOfWarStartTime) {
		this.nextGangOfWarStartTime = nextGangOfWarStartTime;
	}

	public long getNextCountryFlagStartTime() {
		return nextCountryFlagStartTime;
	}

	public void setNextCountryFlagStartTime(long nextCountryFlagStartTime) {
		this.nextCountryFlagStartTime = nextCountryFlagStartTime;
	}

	public long getNextDiplomacyStartTime() {
		return nextDiplomacyStartTime;
	}

	public void setNextDiplomacyStartTime(long nextDiplomacyStartTime) {
		this.nextDiplomacyStartTime = nextDiplomacyStartTime;
	}

	public long getMergeTime() {
		return mergeTime;
	}

	public void setMergeTime(long mergeTime) {
		this.mergeTime = mergeTime;
	}

}
