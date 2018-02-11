package com.mmorpg.mir.model.skill.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

public class EffectControllerDB {
	private List<EffectDB> effectDBs = New.arrayList();
	private Map<String, Long> buffAccumulate = New.hashMap();

	@JsonIgnore
	public void addEffectDB(EffectDB db) {
		effectDBs.add(db);
	}

	public List<EffectDB> getEffectDBs() {
		return effectDBs;
	}

	public void setEffectDBs(List<EffectDB> effectDBs) {
		this.effectDBs = effectDBs;
	}

	public Map<String, Long> getBuffAccumulate() {
		return buffAccumulate;
	}

	public void setBuffAccumulate(Map<String, Long> buffAccumulate) {
		this.buffAccumulate = buffAccumulate;
	}

}
