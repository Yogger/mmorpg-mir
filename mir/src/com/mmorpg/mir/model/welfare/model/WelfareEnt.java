package com.mmorpg.mir.model.welfare.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.entity.PlayerStat;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.JsonUtils;

@Component
public class WelfareEnt extends ModuleHandle {
	@Inject
	private EntityCacheService<Long, PlayerStat> playerStatDbService;

	@Override
	public ModuleKey getModule() {
		return ModuleKey.WELFARE;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getWelfareJson() == null) {
			ent.setWelfareJson(JsonUtils.object2String(Welfare.valueOf(ent.getPlayer())));
		}

		Player player = ent.getPlayer();

		if (player.getWelfare() == null) {
			Welfare welfare = JsonUtils.string2Object(ent.getWelfareJson(), Welfare.class);
			welfare.getSign().setOwner(player);
			welfare.getActiveValue().setOwner(player);
			welfare.getGiftCollect().setOwner(player);
			if (welfare.getAccLoginDays() == null) {
				PlayerStat stat = playerStatDbService.load(ent.getId());
				welfare.setAccLoginDays(stat.getAccLoginNumber());
				welfare.setLastAccLoginNumberTime(stat.getLastAccLoginNumberTime());
			}
			player.setWelfare(welfare);
		}

	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getWelfare() != null) {
			ent.setWelfareJson(JsonUtils.object2String(player.getWelfare()));

		}

	}

}
