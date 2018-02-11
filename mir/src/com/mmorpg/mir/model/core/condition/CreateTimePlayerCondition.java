package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.DateUtils;

public class CreateTimePlayerCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		Date date = DateUtils.string2Date(this.code, DateUtils.PATTERN_DATE_TIME);
		Date createTime = player.getPlayerStat().getCreatedOn();
		String createTimeStr = DateUtils.date2String(createTime, DateUtils.PATTERN_DATE_TIME);
		Date createDate = DateUtils.string2Date(createTimeStr, DateUtils.PATTERN_DATE_TIME);
		if (op == Operator.GREATER_EQUAL) {
			return createDate.after(date) || createTime.equals(date);
		} else if (op == Operator.EQUAL) {
			return createDate.equals(date);
		} else if (op == Operator.LESS_EQUAL) {
			return createDate.before(date) || createTime.equals(date);
		}

		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
