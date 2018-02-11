package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.invest.model.Invest;

public class InvestDrawRewardCondition extends AbstractCoreCondition {

	private Operator op;

	private int type;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getInvestPool().getInvests().containsKey(type)) {
			throw new ManagedException(ManagedErrorCode.INVEST_TYPE_NOT_BUY);
		}

		Invest invest = player.getInvestPool().getInvests().get(type);
		int accLoginDays = invest.getAccLoginDays();
		if (op == Operator.GREATER_EQUAL) {
			return accLoginDays >= this.value;
		} else if (op == Operator.GREATER) {
			return accLoginDays > this.value;
		} else if (op == Operator.EQUAL) {
			return accLoginDays == this.value;
		} else if (op == Operator.LESS) {
			return accLoginDays < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return accLoginDays <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
		this.type = Integer.parseInt(resource.getCode());
	}
}
