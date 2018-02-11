package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;
import com.mmorpg.mir.model.serverstate.ServerState;

public class IdentifyEndCondition extends AbstractCoreCondition {
	@Override
	public boolean verify(Object object) {
		CommonIdentifyTreasureServer treasureServer = ServerState.getInstance().getCommonIdentifyTreasureTotalServers()
				.getTreasureServers().get(code);
		if(!treasureServer.isOpeningIdentifyTreasure()){
			return false;
		}
		return true;
	}
}
