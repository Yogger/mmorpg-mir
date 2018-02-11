package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.invest.model.Invest;
import com.windforce.common.utility.DateUtils;

public class InvestTypeBuyTimeCondition extends AbstractCoreCondition {

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
		int intervalDays = DateUtils.calcIntervalDays(new Date(invest.getBuyTime()), new Date());
		if (op == Operator.GREATER_EQUAL) {
			return intervalDays >= (this.value - 1);
		} else if (op == Operator.GREATER) {
			return intervalDays > (this.value - 1);
		} else if (op == Operator.EQUAL) {
			return intervalDays == (this.value - 1);
		} else if (op == Operator.LESS) {
			return intervalDays < (this.value - 1);
		} else if (op == Operator.LESS_EQUAL) {
			return intervalDays <= (this.value - 1);
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
		this.type = Integer.parseInt(this.code);
	}

}
