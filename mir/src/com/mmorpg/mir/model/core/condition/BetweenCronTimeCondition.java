package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.player.event.OntheHourEvent;
import com.windforce.common.utility.DateUtils;

/**
 * 当前时间是否在区间
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class BetweenCronTimeCondition extends AbstractCoreCondition implements QuestCondition {

	private String startDate;
	private String endDate;

	private long time;

	@Override
	public boolean verify(Object object) {
		long now = time == 0 ? System.currentTimeMillis() : time;
		long start = DateUtils.getNextTime(startDate, new Date(now)).getTime();
		long end = DateUtils.getNextTime(endDate, new Date(now)).getTime();
		if (start >= end) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.TIME_NOT_BETWEEN);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		startDate = resource.getStartDate();
		endDate = resource.getEndDate();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { OntheHourEvent.class };
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
