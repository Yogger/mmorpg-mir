package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Npc;

/**
 * 当前生物出生时间小于多少毫秒
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class NpcLessCreateTimeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Npc npc = null;
		if(object instanceof Npc){
			npc = (Npc) object;
		}
		if(npc == null){
			this.errorObject(object);
		}
		if ((System.currentTimeMillis() - npc.getCreateTime()) < value) {
			return true;
		}
		return false;
	}
}
