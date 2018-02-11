package com.mmorpg.mir.model.util.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;

@Entity
@Cached(size = "ten")
public class IdentifyEntity implements IEntity<String> {

	private static final int STEP = 5000;

	@Id
	@Column(length = 50)
	private String id;
	@Transient
	private long now;

	private long value;

	public static IdentifyEntity valueOf(String id, long value) {
		IdentifyEntity result = new IdentifyEntity();
		result.id = id;
		result.now = value;
		result.value = value;
		return result;
	}

	public String getId() {
		return id;
	}

	public long getNextIdentify() {
		if (now == 0) {
			now = value;
		}
		if (now == value) {
			value += STEP;
		}
		return ++now;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean serialize() {
		return true;
	}

}
