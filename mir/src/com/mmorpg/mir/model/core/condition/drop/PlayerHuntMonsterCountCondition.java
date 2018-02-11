package com.mmorpg.mir.model.core.condition.drop;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.drop.model.MonsterKilledHistory;
import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerHuntMonsterCountCondition extends AbstractCoreCondition {
	/** 运算符 */
	private Operator op;
	private String itemId;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		MonsterKilledHistory mkh = player.getDropHistory().getMonsterKilledHistory();
		if (mkh == null) {
			return false;
		}
		return player.getDropHistory().verify(code, op, itemId, value);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
		this.itemId = resource.getItemId();
	}

	@Override
	protected void calculate(int num) {
		super.calculate(1); // 总共的条件, 不需要计算个数
	}
	
	public String getItemId() {
		return itemId;
	}

}
