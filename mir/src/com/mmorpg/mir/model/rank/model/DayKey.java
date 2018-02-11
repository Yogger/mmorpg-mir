package com.mmorpg.mir.model.rank.model;

import java.util.Date;
import java.util.HashMap;

import org.h2.util.New;

import com.windforce.common.utility.DateUtils;

/**
 * 拿的是当天中午的时间
 * 
 * @author 37wan
 */
public class DayKey {

	private long lunchTime;

	public static final long DIV = 1000 * 600;

	public static DayKey valueOf() {
		DayKey key = new DayKey();
		key.lunchTime = DateUtils.getFirstTime(new Date()).getTime()
				+ org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY / 2;
		return key;
	}

	public static DayKey valueOf(long time) {
		DayKey key = new DayKey();
		key.lunchTime = DateUtils.getFirstTime(new Date(time)).getTime()
				+ org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY / 2;
		return key;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(DayKey.valueOf().equals(DayKey.valueOf()));
		}
		HashMap<Long, Integer> map = New.hashMap();
		map.put(DayKey.valueOf().getLunchTime(), 123);
		map.put(DayKey.valueOf().getLunchTime(), 654);
		map.put(DayKey.valueOf().getLunchTime() - 1, 14321);
		map.put(DayKey.valueOf().getLunchTime() - 1, 3123);
		System.out.println(map.size());
		System.out.println(map.containsKey(DayKey.valueOf().lunchTime));
		System.out.println(map.get(DayKey.valueOf().lunchTime));
		System.out.println(map.get(DayKey.valueOf().lunchTime - 1));

		System.out.println(DayKey.valueOf().getLunchTime());
		System.out.println(System.currentTimeMillis());
		System.out.println(DayKey.valueOf().getLunchTime() + 1);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (lunchTime ^ (lunchTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayKey other = (DayKey) obj;
		if (lunchTime != other.lunchTime)
			return false;
		return true;
	}

	public long getLunchTime() {
		return lunchTime;
	}

	public void setLunchTime(long lunchTime) {
		this.lunchTime = lunchTime;
	}

}
