package com.mmorpg.mir.model.welfare.util;

public enum MonthEnum {

	MONTH_1(1, 31), MONTH_2(2, 28), MONTH_3(3, 31), MONTH_4(4, 30), MONTH_5(5, 31), MONTH_6(6, 30), MONTH_7(7, 31), MONTH_8(
			8, 31), MONTH_9(9, 30), MONTH_10(10, 31), MONTH_11(11, 30), MONTH_12(12, 31);

	final int monthIndex;
	final int monthDays;

	private MonthEnum(int monthIndex, int monthDays) {
		this.monthIndex = monthIndex;
		this.monthDays = monthDays;
	}

	/**
	 * 获取月份天数
	 * 
	 * @param isDouble
	 *            是否闰年
	 * @param month
	 *            当月
	 * @return
	 */
	public static int getDaysOfMonthForYear(boolean isDouble, int month) {
		if (isDouble && month == 2) {// 闰年2月
			return MonthEnum.MONTH_2.monthDays + 1;
		}
		for (MonthEnum m : MonthEnum.values()) {
			if (m.monthIndex == month) {
				return m.monthDays;
			}
		}

		throw new RuntimeException("获取月份天数出错 : [" + isDouble + " , " + month + "]");
	}
}
