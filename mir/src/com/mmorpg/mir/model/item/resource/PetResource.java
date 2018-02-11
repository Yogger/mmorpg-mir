package com.mmorpg.mir.model.item.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class PetResource {

	@Id
	private String id;
	
	private Stat[] stats;
	
	private int canPickup;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public int getCanPickup() {
		return canPickup;
	}

	public void setCanPickup(int canPickup) {
		this.canPickup = canPickup;
	}

	@JsonIgnore
	public boolean canPickUp() {
		return canPickup > 0;
	}
	
}
