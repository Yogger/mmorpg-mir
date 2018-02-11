package com.mmorpg.mir.model.gang.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.gang.model.Gang;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "thousand", persister = @Persister("30s"))
public class GangEnt implements IEntity<Long> {
	@Id
	private long id;
	private String name;

	@Lob
	private String json;

	@Transient
	private transient Gang gang;

	public static GangEnt valueOf(long id, String name) {
		GangEnt ent = new GangEnt();
		ent.id = id;
		ent.name = name;
		return ent;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean serialize() {
		if (gang != null) {
			setJson(JsonUtils.object2String(gang));
			name = gang.getName();
		}
		return true;
	}

	public Gang getGang() {
		if (gang == null) {
			gang = JsonUtils.string2Object(json, Gang.class);
			gang.setGangEnt(this);
		}
		return gang;
	}

	public void setGang(Gang gang) {
		this.gang = gang;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
