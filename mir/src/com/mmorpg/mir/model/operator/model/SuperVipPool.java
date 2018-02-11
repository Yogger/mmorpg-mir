package com.mmorpg.mir.model.operator.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SuperVipPool {
	private Map<String, SuperVip> superVips;

	public static SuperVipPool valueOf() {
		SuperVipPool svp = new SuperVipPool();
		svp.superVips = new HashMap<String, SuperVip>();
		return svp;
	}

	@JsonIgnore
	synchronized public SuperVip getSuperVip(String serverId) {
		if (!superVips.containsKey(serverId)) {
			superVips.put(serverId, SuperVip.valueOf());
		}
		return superVips.get(serverId);
	}

	public Map<String, SuperVip> getSuperVips() {
		return superVips;
	}

	public void setSuperVips(Map<String, SuperVip> superVips) {
		this.superVips = superVips;
	}

}
