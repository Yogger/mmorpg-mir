package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.event.CombatSpiritUpEvent;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class CombatSpiritLevelCondition extends AbstractCoreCondition implements QuestCondition {

	// CombatSpiritType
	private int type;

	private Operator op;

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
		// 阶数
		int value = this.value;
		CombatSpirit combatSpirit = player.getCombatSpiritStorage().getCombatSpiritCollection().get(type);
		if (combatSpirit == null) {
			return false;
		}
		String resourceId = combatSpirit.getCombatResourceId();
		CombatSpiritResource resource = CombatSpiritManager.getInstance().getCombatSpiritResource(resourceId, true);
		if (op == Operator.GREATER) {
			return resource.getLevel() > value;
		} else if (op == Operator.EQUAL) {
			return resource.getLevel() == value;
		} else if (op == Operator.LESS) {
			return resource.getLevel() < value;
		} else if (op == Operator.GREATER_EQUAL) {
			return resource.getLevel() >= value;
		} else if (op == Operator.LESS_EQUAL) {
			return resource.getLevel() <= value;
		}

		return false;
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		type = Integer.parseInt(resource.getCode());
		this.op = resource.getOperatorEnum();
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { CombatSpiritUpEvent.class };
	}
}
