package com.mmorpg.mir.model.core.condition;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.windforce.common.utility.DateUtils;

/**
 * code 活动开始的时间
 * value 活动持续的时间
 * @author 37wan
 */
public class ActivityInPeriodCondition extends AbstractCoreCondition {

	
	@Override
	public boolean verify(Object object) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		Date now = new Date();
		Date beginTime = DateUtils.string2Date(this.code, pattern);
		
		if (now.getTime() < beginTime.getTime() || now.getTime() - beginTime.getTime() > value) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_PERIOD); 
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		System.out.println(timeFormat.format(now));
	}

}
