package com.mmorpg.mir.model.core.condition;

import java.util.Calendar;

/**
 * 
 * @author 37.com
 * 
 */
public class YearOfDayRemainCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Calendar c = Calendar.getInstance();
		int days = c.get(Calendar.DAY_OF_YEAR);
		int codeInt = Integer.parseInt(this.code);
		if (codeInt == 0) {
			throw new IllegalArgumentException("条件类型[YEAR_OF_DAY_REMAIN]code值不能为0");
		}
		return days % codeInt == value;
	}
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		System.out.println(c.get(Calendar.DAY_OF_YEAR));
	}
}
