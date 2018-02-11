package com.mmorpg.mir.model.welfare.util;

import com.windforce.common.utility.DateUtils;

public class Util {

	private Util() {
	}

	private static class Instance {
		private static Util instance = new Util();
	}

	public static Util getInstance() {
		return Instance.instance;
	}

	public java.util.Calendar getCalendar(long millis) {
		final java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(new java.util.Date(millis));
		return calendar;
	}

	/**
	 * @param millis
	 * @return [年,月,日,星期]
	 */
	public int[] currentDate(long millis) {
		final java.util.Calendar calendar = getCalendar(millis);
		return new int[] { calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH) + 1,
				calendar.get(java.util.Calendar.DAY_OF_MONTH), calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1 };
	}

	public int getDaysOfMonthForYear(long millis) {
		return MonthEnum.getDaysOfMonthForYear(getYear(millis) % 4 == 0, getMonth(millis));
	}

	public boolean isSameMonth(long nowMillis, long targetMillis) {
		return getYear(nowMillis) == getYear(targetMillis) && getMonth(nowMillis) == getMonth(targetMillis);
	}

	public boolean isSameDay(long nowMillis, long targetMillis) {
		final int[] timeDate = currentDate(nowMillis);
		final int[] targetDate = currentDate(targetMillis);
		int i = 0;
		while (i < timeDate.length) {
			if (timeDate[i] != targetDate[i]) {
				return false;
			}
			i++;
		}
		return true;
	}

	/**
	 * @param nowMillis
	 * @param targetMillis
	 * @return nowMillis 是否比 targetMillis 大出至少一个月,也许刚好是1号和上个月29号
	 */
	public boolean passMonth(long nowMillis, long targetMillis) {
		return getYear(nowMillis) == getYear(targetMillis) && getMonth(nowMillis) - getMonth(targetMillis) > 0;
	}

	public int getYear(long millis) {
		return getCalendar(millis).get(java.util.Calendar.YEAR);
	}

	public int getMonth(long millis) {
		return getCalendar(millis).get(java.util.Calendar.MONTH) + 1;
	}

	public int getDay(long millis) {
		return getCalendar(millis).get(java.util.Calendar.DAY_OF_MONTH);
	}

	public int getWeek(long millis) {
		return getCalendar(millis).get(java.util.Calendar.DAY_OF_WEEK) - 1;
	}

	public static void main(String[] args) {
		 java.util.Date date = DateUtils.string2Date("2014-10-16 12:00:00",
		 "yyyy-MM-dd HH:mm:ss");
		 long last = date.getTime();
		 long now = System.currentTimeMillis();
		 System.out.println(getInstance().passMonth(now, last));
		
		 int[] dates = getInstance().currentDate(System.currentTimeMillis());
		 System.out.println(java.text.MessageFormat.format("今天是[{0}]年[{1}]月[{2}]日,星期[{3}]",
		 "" + dates[0], "" + dates[1], ""
		 + dates[2], "" + dates[3]));
		 System.out.println(dates[0]);
		
	}
}
