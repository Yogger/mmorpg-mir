package com.mmorpg.mir.model.player.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.controllers.effect.PlayerEffectController;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PlayerEffectControllInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.EFFECTCONTROLL;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getEffectControllerDBJson() == null) {
			PlayerEffectController controller = new PlayerEffectController(ent.getPlayer());
			ent.setEffectControllerDBJson(JsonUtils.object2String(controller.createEffectControllerDB()));
		}

		Player player = ent.getPlayer();

		if (player.getEffectController() == null) {
			player.setEffectController(new PlayerEffectController(player));
			// 不恢复BUFF,再登录的时候才恢复BUFF
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		// 下线的时候存储，并且移除。这里如若再存储会产生覆盖
		// ((PlayerEffectController)ent.getPlayer().getEffectController()).save();
	}
}
