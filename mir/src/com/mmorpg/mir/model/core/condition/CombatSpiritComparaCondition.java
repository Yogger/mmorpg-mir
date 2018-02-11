package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.event.CombatSpiritUpEvent;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class CombatSpiritComparaCondition extends AbstractCoreCondition implements QuestCondition {

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
		CombatSpiritResource resource = CombatSpiritManager.getInstance().getCombatSpiritResource(code, true);
		CombatSpirit combatSpirit = player.getCombatSpiritStorage().getCombatSpiritCollection().get(resource.getType().getValue());
		if (combatSpirit == null) {
			return false;
		}
		String resourceId = combatSpirit.getCombatResourceId();
		CombatSpiritResource myResource = CombatSpiritManager.getInstance().getCombatSpiritResource(resourceId, true);
		
		if (op == Operator.GREATER) {
			if (myResource.getQuality() > resource.getQuality()) {
				return true;
			} else {
				return myResource.getLevel() > resource.getLevel();
			}
		} else if (op == Operator.EQUAL) {
			return myResource.getLevel() == resource.getLevel() && myResource.getQuality() == resource.getQuality();
		} else if (op == Operator.LESS) {
			return myResource.getLevel() < resource.getLevel() && myResource.getQuality() <= resource.getQuality();
		} else if (op == Operator.GREATER_EQUAL) {
			if (myResource.getQuality() > resource.getQuality()) {
				return true;
			}
			return myResource.getQuality() == resource.getQuality() && myResource.getLevel() >= resource.getLevel();
		} else if (op == Operator.LESS_EQUAL) {
			return myResource.getLevel() <= resource.getLevel() && myResource.getQuality() <= resource.getQuality();
		}

		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { CombatSpiritUpEvent.class };
	}

}
