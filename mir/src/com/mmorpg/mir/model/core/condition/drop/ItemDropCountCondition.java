package com.mmorpg.mir.model.core.condition.drop;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.serverstate.ServerState;

public class ItemDropCountCondition extends AbstractCoreCondition {

	/** 运算符 */
	private Operator op;

	@Override
	public boolean verify(Object object) {
		return ServerState.getInstance().itemDropCountVerify(code, op, value);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	protected void calculate(int num) {
		super.calculate(1); // 总共的条件, 不需要计算个数
	}

}
