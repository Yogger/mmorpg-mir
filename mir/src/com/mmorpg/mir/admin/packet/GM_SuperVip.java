package com.mmorpg.mir.admin.packet;

import java.util.Set;

import com.mmorpg.mir.model.operator.model.SuperVip;

public class GM_SuperVip {

	private Set<SuperVip> superVips;

	public Set<SuperVip> getSuperVips() {
		return superVips;
	}

	public void setSuperVips(Set<SuperVip> superVips) {
		this.superVips = superVips;
	}

}
