package com.mmorpg.mir.model.suicide.resource;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SuicideStatResource {

	@Id
	private Integer id;

	private Stat[] stats;

	public Integer getId() {
		return id;
	}

	@JsonIgnore
	public List<Stat> multiRate(double rate) {
		List<Stat> newStats = New.arrayList();
		for (Stat s : this.stats) {
			Stat newStat = s.getNewProperty();
			newStat.setValueA((int) (newStat.getValueA() * rate));
			newStat.setValueB((int) (newStat.getValueB() * rate));
			newStat.setValueC((int) (newStat.getValueC() * rate));
			newStats.add(newStat);
		}
		return newStats;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

}
