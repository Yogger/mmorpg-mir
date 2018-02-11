package com.mmorpg.mir.model.operator.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class GmPrivilege {
	private transient Player owner;
	private boolean gm;
	private HashSet<Integer> privileges;
	private boolean openHide;

	@JsonIgnore
	public boolean havePrivilege(GmPrivilegeType type) {
		return privileges.contains(type.getValue());
	}

	public boolean isGm() {
		return gm;
	}

	public void setGm(boolean gm) {
		this.gm = gm;
	}

	public static GmPrivilege valueOf() {
		GmPrivilege gmPrivilege = new GmPrivilege();
		gmPrivilege.setPrivileges(new HashSet<Integer>());
		return gmPrivilege;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public HashSet<Integer> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(HashSet<Integer> privileges) {
		this.privileges = privileges;
	}

	public boolean isOpenHide() {
		return openHide;
	}

	public void setOpenHide(boolean openHide) {
		this.openHide = openHide;
	}

}
