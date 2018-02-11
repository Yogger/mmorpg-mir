package com.mmorpg.mir.model.kingofwar.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.kingofwar.model.KingOfWarInfo;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class KingOfWarEnt implements IEntity<Integer> {
	@Id
	private int id;
	@Lob
	private String kingOfWarInfoJson;

	@Transient
	private KingOfWarInfo kingOfWarInfo;

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean serialize() {
		kingOfWarInfoJson = JsonUtils.object2String(kingOfWarInfo);
		return true;
	}

	public String getKingOfWarInfoJson() {
		return kingOfWarInfoJson;
	}

	public void setKingOfWarInfoJson(String kingOfWarInfoJson) {
		this.kingOfWarInfoJson = kingOfWarInfoJson;
	}

	public KingOfWarInfo getKingOfWarInfo() {
		if (kingOfWarInfo == null) {
			kingOfWarInfo = JsonUtils.string2Object(kingOfWarInfoJson, KingOfWarInfo.class);
		}
		return kingOfWarInfo;
	}

	public void setKingOfWarInfo(KingOfWarInfo kingOfWarInfo) {
		this.kingOfWarInfo = kingOfWarInfo;
	}

}
