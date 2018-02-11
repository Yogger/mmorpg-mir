package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.utility.DateUtils;

public class ServerOpenTimeCondition extends AbstractCoreCondition implements QuestCondition{

	private Operator op;
	
	@Override
	public boolean verify(Object object) {
		Date openDate = ServerState.getInstance().getOpenServerDate();
		if (openDate != null) {
			long interval = ((System.currentTimeMillis() - openDate.getTime()) / DateUtils.MILLIS_PER_MINUTE);
			
			if (op == Operator.GREATER_EQUAL) {
				return interval >= value;
			} else if (op == Operator.GREATER) {
				return interval > value;
			} else if (op == Operator.EQUAL) {
				return interval == value;
			} else if (op == Operator.LESS_EQUAL) {
				return interval <= value;
			} else if (op == Operator.LESS) {
				return interval < value;
			}
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] {};
	}
}
