package com.mmorpg.mir.model.copy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_MingJiangBoss_Reset {
	private String id;
	
	private Reward drop;
	
	public static SM_MingJiangBoss_Reset valueOf(String id, Reward drop){
		SM_MingJiangBoss_Reset sm = new SM_MingJiangBoss_Reset();
		sm.id = id;
		sm.drop = drop;
		return sm;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Reward getDrop() {
		return drop;
	}

	public void setDrop(Reward drop) {
		this.drop = drop;
	}
}
