package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.purse.event.CurrencyRewardEvent;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.quest.model.Quest;

public class CurrencyTotalCondition extends AbstractCoreCondition implements NicknameCondition, QuestCondition {

	private CurrencyType type;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			Quest quest = (Quest) object;
			player = quest.getOwner();
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getPurse().isEnoughTotal(type, value)) {
			// 这里根据类型抛出异常
			throw new ManagedException(getType().getErrorCode());
		}
		return true;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	protected void init() {
		setType(CurrencyType.valueOf(Integer.valueOf(code)));
	}

	@Override
	protected void calculate(int num) {
		super.calculate(1); // 总共的条件, 不需要计算个数
	}

	public CurrencyType getType() {
		return type;
	}

	public void setType(CurrencyType type) {
		this.type = type;
	}

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] { CurrencyRewardEvent.class };
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { CurrencyRewardEvent.class };
	}

}
