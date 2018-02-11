package com.mmorpg.mir.model.complexstate;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class ComplexStateInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.COMPLEXSTATE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getComplexStateJson() == null) {
			ent.setComplexStateJson(JsonUtils.object2String(ComplexState.valueOf()));
		}
		Player player = ent.getPlayer();

		if (player.getComplexState() == null) {
			player.setComplexState(JsonUtils.string2Object(ent.getComplexStateJson(), ComplexState.class));
			player.getComplexState().isState(ComplexStateType.FRIEND);
		}

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getComplexState() != null) {
			player.getComplexState().bitSet2ByteArray();
			ent.setComplexStateJson(JsonUtils.object2String(player.getComplexState()));
		}

	}
}
