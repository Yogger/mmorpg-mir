package com.mmorpg.mir.model.promote.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class PromotionInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.PROMOTION;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getPromotionJson() == null) {
			ent.setPromotionJson(JsonUtils.object2String(Promotion.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getPromotion() == null) {
			Promotion p = JsonUtils.string2Object(ent.getPromotionJson(), Promotion.class);
			player.setPromotion(p);
			p.setOwner(player);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getPromotion() != null) {
			ent.setPromotionJson(JsonUtils.object2String(player.getPromotion()));
		}
	}

}
