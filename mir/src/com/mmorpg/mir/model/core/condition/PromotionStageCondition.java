package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.model.SkillCondition;

public class PromotionStageCondition extends AbstractCoreCondition implements QuestCondition, SkillCondition {

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

		boolean ret = true;
		if (code.trim().equals("<=")) {
			ret = player.getPromotion().getStage() <= value;
		} else if (code.trim().equals(">=")) {
			ret = player.getPromotion().getStage() >= value;
		} else if (code.trim().equals("=")) {
			ret = player.getPromotion().getStage() == value;
		} else if (code.trim().equals(">")) {
			ret = player.getPromotion().getStage() > value;
		} else if (code.trim().equals("<")) {
			ret = player.getPromotion().getStage() < value;
		}
		if (!ret) {
			throw new ManagedException(ManagedErrorCode.PROMOTION_STAGE_NOT_SATIESFIED);
		}

		return ret;

	}

	@Override
	public Class<?>[] getEvent() {
		return new Class[] { PromotionEvent.class };
	}

	@Override
	public Class<?>[] getSkillEvent() {
		return new Class[] { PromotionEvent.class };
	}

}
