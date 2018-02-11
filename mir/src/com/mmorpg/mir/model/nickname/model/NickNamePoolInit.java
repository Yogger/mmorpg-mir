package com.mmorpg.mir.model.nickname.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class NickNamePoolInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.NICKNAME;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getNickNameJson() == null) {
			ent.setNickNameJson(JsonUtils.object2String(NicknamePool.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getNicknamePool() == null) {
			NicknamePool nickNamePool = JsonUtils.string2Object(ent.getNickNameJson(), NicknamePool.class);
			player.setNicknamePool(nickNamePool);
			nickNamePool.setOwner(player);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getNicknamePool() != null) {
			ent.setNickNameJson(JsonUtils.object2String(player.getNicknamePool()));
		}		
	}

}
