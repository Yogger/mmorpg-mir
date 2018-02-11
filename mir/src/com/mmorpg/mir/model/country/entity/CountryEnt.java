package com.mmorpg.mir.model.country.entity;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.TraitorPlayer;
import com.mmorpg.mir.model.country.model.TraitorPlayerFix;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class CountryEnt implements IEntity<String> {

	@Id
	@Column(length = 50)
	private String id;

	@Lob
	private String countryJson;

	@Transient
	private Country country;

	public Country createCountry() {
		if (country == null) {
			country = JsonUtils.string2Object(countryJson, Country.class);
		}
		if (country.getTraitorMapFixs() == null) {
			country.setTraitorMapFixs(new ConcurrentHashMap<Long, TraitorPlayerFix>());
			country.setTraitorMap(new ConcurrentHashMap<Long, TraitorPlayer>());
		}
		country.getCountryQuest().initNewFeature();
		return country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryJson() {
		return countryJson;
	}

	public void setCountryJson(String countryJson) {
		this.countryJson = countryJson;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public boolean serialize() {
		if (country != null) {
			countryJson = JsonUtils.object2String(country);
		}
		return true;
	}

}
