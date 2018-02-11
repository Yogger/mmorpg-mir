package com.mmorpg.mir.model.util.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;

@Entity
@Cached(size = "ten")
public class StateEntity implements IEntity<String> {

	@Id
	@Column(length = 50)
	private String id;

	private int mergeState;

	public static StateEntity valueOf(String id) {
		StateEntity result = new StateEntity();
		result.id = id;
		result.mergeState = 0;
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMergeState() {
		return mergeState;
	}

	public void setMergeState(int mergeState) {
		this.mergeState = mergeState;
	}

	@Override
	public boolean serialize() {
		return true;
	}

}
