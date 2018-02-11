package com.mmorpg.mir.model.rank.model.rank;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.rank.entity.OpenServerCountryRankEnt;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;

public class OpenServerCountryRank {

	private int countryValue;

	private Map<Long, Integer> powers;

	@JsonIgnore
	private ReentrantLock lock = new ReentrantLock();

	public static OpenServerCountryRank valueOf(int id) {
		OpenServerCountryRank op = new OpenServerCountryRank();
		op.countryValue = id;
		op.powers = new HashMap<Long, Integer>();
		return op;
	}

	@JsonIgnore
	public long calcPower() {
		try {
			lock.lock();
			long countryPower = 0L;
			for (Entry<Long, Integer> entry : powers.entrySet()) {
				countryPower += entry.getValue();
			}
			return countryPower;
		} finally {
			lock.unlock();
		}
	}

	@JsonIgnore
	public void battleScoreHandler(BattleScoreRefreshEvent event) {
		try {
			lock.lock();
			Integer mePower = powers.get(event.getOwner());
			if (mePower == null) {
				if (powers.size() < 50) {
					powers.put(event.getOwner(), event.getBattleScore());
				} else {
					Long min = null;
					Integer minValue = Integer.MAX_VALUE;
					for (Entry<Long, Integer> entry : powers.entrySet()) {
						if (entry.getValue() < minValue) {
							min = entry.getKey();
							minValue = entry.getValue();
						}
					}
					if (event.getBattleScore() > minValue) {
						powers.remove(min);
						powers.put(event.getOwner(), event.getBattleScore());
					}
				}
			} else if (event.getBattleScore() > mePower) {
				powers.put(event.getOwner(), event.getBattleScore());
			}
			update();
		} finally {
			lock.unlock();
		}
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

	public Map<Long, Integer> getPowers() {
		return powers;
	}

	public void setPowers(Map<Long, Integer> powers) {
		this.powers = powers;
	}

	@JsonIgnore
	public void setRankEnt(OpenServerCountryRankEnt ent) {
		countryRankEnt = ent;
	}

	@JsonIgnore
	private OpenServerCountryRankEnt countryRankEnt;

	@JsonIgnore
	public OpenServerCountryRankEnt getRankEnt() {
		return countryRankEnt;
	}

	@JsonIgnore
	public void update() {
		WorldRankManager.getInstance().updateOpenServerCountryRank(this);
	}
}
