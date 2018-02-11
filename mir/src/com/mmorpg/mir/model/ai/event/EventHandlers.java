package com.mmorpg.mir.model.ai.event;

import com.mmorpg.mir.model.ai.event.handler.AttackedEventHandler;
import com.mmorpg.mir.model.ai.event.handler.BackHomeEventHandler;
import com.mmorpg.mir.model.ai.event.handler.DeleteEventHandler;
import com.mmorpg.mir.model.ai.event.handler.DespawnEventHandler;
import com.mmorpg.mir.model.ai.event.handler.DiedEventHandler;
import com.mmorpg.mir.model.ai.event.handler.FarFromHomeEventHandler;
import com.mmorpg.mir.model.ai.event.handler.MostHatedChangedEventHandler;
import com.mmorpg.mir.model.ai.event.handler.NotSeePlayerEventHandler;
import com.mmorpg.mir.model.ai.event.handler.NotSeePlayerSummonEventHandler;
import com.mmorpg.mir.model.ai.event.handler.NothingTodoEventHandler;
import com.mmorpg.mir.model.ai.event.handler.RestoredHealthEventHandler;
import com.mmorpg.mir.model.ai.event.handler.RouteOverEventHandler;
import com.mmorpg.mir.model.ai.event.handler.SeePlayerEventHandler;
import com.mmorpg.mir.model.ai.event.handler.SpawnEventHandler;
import com.mmorpg.mir.model.ai.event.handler.TiredAttackingEventHandler;

public enum EventHandlers {
	/** 被攻击 **/
	ATTACKED_EH(new AttackedEventHandler()),
	/** 厌倦攻击 **/
	TIREDATTACKING_EH(new TiredAttackingEventHandler()),
	/** 更换仇恨最高的生物 **/
	MOST_HATED_CHANGED_EH(new MostHatedChangedEventHandler()),
	/** 看到玩家 **/
	SEEPLAYER_EH(new SeePlayerEventHandler()),
	/** 失去玩家 **/
	NOTSEEPLAYER_EH(new NotSeePlayerEventHandler()),
	/** 失去玩家 **/
	NOTSEEPLAYER_SUMMON_EH(new NotSeePlayerSummonEventHandler()),
	/** 离家太远 **/
	FARFROMHOME_EH(new FarFromHomeEventHandler()),
	/** 回到出生点 **/
	BACKHOME_EH(new BackHomeEventHandler()),
	/** 路线结束 **/
	ROUTEOVER_EH(new RouteOverEventHandler()),
	/** 什么也不做 **/
	NOTHINGTODO_EH(new NothingTodoEventHandler()),
	/** 从地图上销毁 **/
	DESPAWN_EH(new DespawnEventHandler()),
	/** 从地图上生成 **/
	SPAWN_EH(new SpawnEventHandler()),
	/** 怪物回满血以后 **/
	RESTOREDHEALTH_EH(new RestoredHealthEventHandler()),
	/** 死亡 **/
	DIED_EH(new DiedEventHandler()),
	/** 删除 **/
	DELETE_EH(new DeleteEventHandler());

	private IEventHandler eventHandler;

	private EventHandlers(IEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public IEventHandler getHandler() {
		return eventHandler;
	}
}
