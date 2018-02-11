package com.mmorpg.mir.model.rank.model.rank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.rank.entity.CountryRankEnt;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.windforce.common.utility.JsonUtils;

public class CountryRank {

	private int countryValue;

	/** 日期,<前50名的ID,对应的最高战斗力> */
	private Map<Long, Map<Long, Integer>> power;

	@JsonIgnore
	private ReentrantLock lock;

	public static CountryRank valueOf(int countryValue) {
		CountryRank rank = new CountryRank();
		rank.lock = new ReentrantLock();
		rank.countryValue = countryValue;
		rank.power = new HashMap<Long, Map<Long, Integer>>();
		return rank;
	}

	@JsonIgnore
	public void initLock() {
		if (lock == null) {
			lock = new ReentrantLock();
		}
	}

	@JsonIgnore
	public long refreshAndCalcPower() {
		Long key = DayKey.valueOf().getLunchTime();
		try {
			lock.lock();
			List<Long> removeIds = New.arrayList();
			// refresh
			for (Entry<Long, Map<Long, Integer>> entry : power.entrySet()) {
				if (key - entry.getKey() > 7 * DateUtils.MILLIS_PER_DAY) {//) {
					removeIds.add(entry.getKey());
				}
			}
			for (Long idKey: removeIds) {
				power.remove(idKey);
			}
			
			// calc
			Long lastDay = key - DateUtils.MILLIS_PER_DAY;
			long countryPower = 0;
			Map<Long, Integer> beforePowerMap = power.get(lastDay);
			if (beforePowerMap != null) {
				for (Entry<Long, Integer> entry : beforePowerMap.entrySet()) {
					countryPower += entry.getValue();
				}
			}
			update();
			return countryPower;
		} finally {
			lock.unlock();
		}
	}

	@JsonIgnore
	public void battleScoreHandler(BattleScoreRefreshEvent event) {
		Long key = DayKey.valueOf().getLunchTime();
		try {
			lock.lock();
			Map<Long, Integer> powerMap = power.get(key);
			if (powerMap == null) {
				Map<Long, Integer> calcMap = New.hashMap();
				calcMap.put(event.getOwner(), event.getBattleScore());
				power.put(key, calcMap);
			} else {
				Integer playerPower = powerMap.get(event.getOwner());
				if (playerPower == null) {
					if (powerMap.size() < 50) {
						powerMap.put(event.getOwner(), event.getBattleScore());
					} else {
						Long min = null;
						Integer minValue = Integer.MAX_VALUE;
						for (Entry<Long, Integer> entry : powerMap.entrySet()) {
							if (entry.getValue() < minValue) {
								min = entry.getKey();
								minValue = entry.getValue();
							}
						}
						if (event.getBattleScore() > minValue) {
							powerMap.remove(min);
							powerMap.put(event.getOwner(), event.getBattleScore());
						}
					}
				} else if (playerPower < event.getBattleScore()) {
					powerMap.put(event.getOwner(), event.getBattleScore());
				}
				
			}
			update();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		ConcurrentHashMap<Integer, Integer> h = new ConcurrentHashMap<Integer, Integer>();
		h.put(123, 123);
		String s = JsonUtils.object2String(h);
		System.out.println(s);
		Map<String, Object> gh = JsonUtils.string2Map(s);
		System.out.println(gh);
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

	public Map<Long, Map<Long, Integer>> getPower() {
		return power;
	}

	public void setPower(Map<Long, Map<Long, Integer>> power) {
		this.power = power;
	}

	@JsonIgnore
	private CountryRankEnt countryRankEnt;
	
	@JsonIgnore
	public CountryRankEnt getRankEnt() {
	    return countryRankEnt;
    }
	
	@JsonIgnore
	public void setRankEnt(CountryRankEnt ent) {
		countryRankEnt = ent;
	}

	@JsonIgnore
	public void update() {
		WorldRankManager.getInstance().updateCountryRank(this);
	}
}
