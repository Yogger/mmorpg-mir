package com.mmorpg.mir.model.rank.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.rank.model.rank.CountryRank;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30m"))
public class CountryRankEnt implements IEntity<Integer> {

	/** 国家ID */
	@Id
	private Integer id;

	@Lob
	private String countryRankJson;

	@Transient
	private CountryRank countryRank;

	public static CountryRankEnt valueOf(Integer id) {
		CountryRankEnt ent = new CountryRankEnt();
		ent.id = id;
		ent.countryRankJson = JsonUtils.object2String(CountryRank.valueOf(id));
		return ent;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public boolean serialize() {
		if (countryRank != null) {
			countryRankJson = JsonUtils.object2String(countryRank);
		}
		return true;
	}

	@JsonIgnore
	public CountryRank getCountryRank() {
		if (countryRank == null) {
			countryRank = JsonUtils.string2Object(countryRankJson, CountryRank.class);
			countryRank.initLock();
			countryRank.setRankEnt(this);
		}
		return countryRank;
	}

	public String getCountryRankJson() {
		return countryRankJson;
	}

	public void setCountryRankJson(String countryRankJson) {
		this.countryRankJson = countryRankJson;
	}

}
