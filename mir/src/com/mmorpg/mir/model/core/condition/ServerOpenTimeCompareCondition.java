package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.utility.DateUtils;

public class ServerOpenTimeCompareCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Date openDate = ServerState.getInstance().getOpenServerDate();
		if (openDate == null) {
			return false;
		}
		Date openServerTime = ServerState.getInstance().getOpenServerDate();
		String openServerTimeStr = DateUtils.date2String(openServerTime, DateUtils.PATTERN_DATE_TIME);
		Date openServerTimeValue = DateUtils.string2Date(openServerTimeStr, DateUtils.PATTERN_DATE_TIME);

		Date valDate = DateUtils.string2Date(this.code, DateUtils.PATTERN_DATE_TIME);
		if (op == Operator.EQUAL) {
			return openServerTimeValue.getTime() == valDate.getTime();
		} else if (op == Operator.GREATER) {
			return openServerTimeValue.getTime() > valDate.getTime();
		} else if (op == Operator.GREATER_EQUAL) {
			return openServerTimeValue.getTime() >= valDate.getTime();
		} else if (op == Operator.LESS) {
			return openServerTimeValue.getTime() < valDate.getTime();
		} else if (op == Operator.LESS_EQUAL) {
			return openServerTimeValue.getTime() <= valDate.getTime();
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
