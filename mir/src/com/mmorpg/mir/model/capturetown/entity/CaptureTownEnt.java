package com.mmorpg.mir.model.capturetown.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.capturetown.model.TownSchema;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class CaptureTownEnt implements IEntity<Integer> {
	@Id
	private Integer id;
	
	@Lob
	private String townInfoJson;
	
	@Transient
	private TownSchema townSchema;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean serialize() {
		if (townSchema != null) {
			townInfoJson = JsonUtils.object2String(townSchema);
		}
		return true;
	}
	
	@JsonIgnore
	public TownSchema getTownSchema() {
		if (townSchema == null) {
			townSchema = JsonUtils.string2Object(townInfoJson, TownSchema.class);
		}
		return townSchema;
	}
	
	public String getTownInfoJson() {
		return townInfoJson;
	}

	public void setTownInfoJson(String townInfoJson) {
		this.townInfoJson = townInfoJson;
	}

}
