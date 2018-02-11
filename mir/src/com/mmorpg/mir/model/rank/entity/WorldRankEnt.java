package com.mmorpg.mir.model.rank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.rank.model.rank.WorldRank;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;

@Entity
@Cached(size = "thousand", persister = @Persister("30m"))
public class WorldRankEnt implements IEntity<String> {

	@Id
	@Column(length = 50)
	private String id;

	@Lob
	private String worldRankJson;

	@Transient
	private WorldRank worldRank;

	@Override
	public String getId() {
		return id;
	}

	public static WorldRankEnt valueOf(RankType type) {
		WorldRankEnt ent = new WorldRankEnt();
		ent.id = type.name();
		ent.worldRankJson = WorldRank.serializeToString(new WorldRank(type));
		return ent;
	}

	@Override
	public boolean serialize() {
		if (worldRank != null) {
			worldRankJson = WorldRank.serializeToString(worldRank);
		}
		return true;
	}

	@JsonIgnore
	public WorldRank getWorldRank() {
		if (worldRank == null) {
			worldRank = WorldRank.deserializeToObject(worldRankJson);
			worldRank.initLock();
			worldRank.setRankEnt(this);
		}
		return worldRank;
	}

	public String getWorldRankJson() {
		return worldRankJson;
	}

	public void setWorldRankJson(String worldRankJson) {
		this.worldRankJson = worldRankJson;
	}

}
