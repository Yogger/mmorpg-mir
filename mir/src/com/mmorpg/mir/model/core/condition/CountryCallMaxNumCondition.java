package com.mmorpg.mir.model.core.condition;

/** 最多响应X个玩家，当超过X个玩家点击召集令后，其他玩家需要排 */
public class CountryCallMaxNumCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		//TODO 限定进入玩家数量Condition
		return true;
	}

}
