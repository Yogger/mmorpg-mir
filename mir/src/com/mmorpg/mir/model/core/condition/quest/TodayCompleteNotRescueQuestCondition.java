package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.vip.event.VipEvent;
import com.mmorpg.mir.model.welfare.event.RescueClawbackEvent;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;

/**
 * 营救完成任务的次数少于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class TodayCompleteNotRescueQuestCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if(player == null){
			this.errorObject(object);
		}
		// 加上福利大厅的找回次数限制,Vip次数
		int questResuceCompleteCount = player.getRescue().getTodayCompleteCount()
				- player.getVip().getResource().getExRescueCount()
				- player.getWelfare().getWelfareHistory().getClawbackNum(ClawbackEnum.CLAWBACK_EVENT_RESCUE);
		if (questResuceCompleteCount < value) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { RescueClawbackEvent.class, VipEvent.class };
	}
}
