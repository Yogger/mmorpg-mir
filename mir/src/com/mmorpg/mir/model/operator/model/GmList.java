package com.mmorpg.mir.model.operator.model;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class GmList {
	private Set<Long> gmList;

	public static GmList valueOf() {
		GmList fcl = new GmList();
		fcl.gmList = new HashSet<Long>();
		return fcl;
	}

	@JsonIgnore
	synchronized public void addGm(Player player) {
		gmList.add(player.getObjectId());
	}

	@JsonIgnore
	synchronized public boolean removeGm(Long objectId) {
		return gmList.remove(objectId);
	}

	@JsonIgnore
	synchronized public boolean isGM(long playerId) {
		return gmList.contains(playerId);
	}

	public Set<Long> getGmList() {
		return gmList;
	}

	public void setGmList(Set<Long> gmList) {
		this.gmList = gmList;
	}

}
