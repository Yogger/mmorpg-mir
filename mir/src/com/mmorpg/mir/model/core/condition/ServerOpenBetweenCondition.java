package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.utility.DateUtils;

public class ServerOpenBetweenCondition extends AbstractCoreCondition {

	private int low;
	private int high;

	private String startTimePattern;
	private String endTimePattern;

	@Override
	public boolean verify(Object object) {
		Date openTime = ServerState.getInstance().getOpenServerDate();
		if (openTime == null) {
			return false;
		}
		Date startTime = DateUtils.addDays(openTime, this.low);
		Date endTime = DateUtils.addDays(openTime, this.high);

		startTime = DateUtils.string2Date(DateUtils.date2String(startTime, this.startTimePattern),
				DateUtils.PATTERN_DATE_TIME);
		endTime = DateUtils.string2Date(DateUtils.date2String(endTime, this.endTimePattern),
				DateUtils.PATTERN_DATE_TIME);
		return System.currentTimeMillis() >= startTime.getTime() && System.currentTimeMillis() <= endTime.getTime();
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
		this.startTimePattern = resource.getStartDate();
		this.endTimePattern = resource.getEndDate();
	}

	public static void main(String[] args) {
		Date now = new Date();
		String d = "yyyy-MM-dd 20:00:00";
		String o = DateUtils.date2String(now, d);
		System.err.println(o);
	}
}
