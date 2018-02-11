package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.windforce.common.utility.DateUtils;

/**
 * 当前时间是否在期间条件
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class BetweenTimeCondition extends AbstractCoreCondition {

	private long startTime;
	private long endTime;

	@Override
	public boolean verify(Object object) {
		long now = System.currentTimeMillis();
		if (now >= startTime && now <= endTime) {
			return true;
		}
		if (isThrowException()) {
			throw new ManagedException(ManagedErrorCode.TIME_NOT_BETWEEN);
		} else {
			return false;
		}
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.startTime = DateUtils.string2Date(resource.getStartDate(), DateUtils.PATTERN_DATE_TIME).getTime();
		this.endTime = DateUtils.string2Date(resource.getEndDate(), DateUtils.PATTERN_DATE_TIME).getTime();
	}

	@Override
	protected void calculate(int num) {
		// nothing todo
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
