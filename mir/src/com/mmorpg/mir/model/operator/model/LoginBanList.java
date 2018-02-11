package com.mmorpg.mir.model.operator.model;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class LoginBanList {
	private Set<BanPlayer> banList;

	public static LoginBanList valueOf() {
		LoginBanList fcl = new LoginBanList();
		fcl.banList = new HashSet<BanPlayer>();
		return fcl;
	}

	@JsonIgnore
	synchronized public void addBan(Player player) {
		banList.add(BanPlayer.valueOf(player));
	}

	@JsonIgnore
	synchronized public boolean unBan(Long objectId) {
		BanPlayer bp = new BanPlayer();
		bp.setPlayerId(objectId);
		return banList.remove(bp);
	}

	@JsonIgnore
	synchronized public boolean isBan(long playerId) {
		BanPlayer bp = new BanPlayer();
		bp.setPlayerId(playerId);
		return banList.contains(bp);
	}

	public Set<BanPlayer> getBanList() {
		return banList;
	}

	public void setBanList(Set<BanPlayer> banList) {
		this.banList = banList;
	}

}
