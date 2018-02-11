package com.mmorpg.mir.model.player.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.LevelLog;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.entity.PlayerStat;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.JsonUtils;

@Component
public class StatInit extends ModuleHandle {

	@Inject
	private EntityCacheService<Long, PlayerStat> playerStatDbService;

	@Override
	public ModuleKey getModule() {
		return ModuleKey.STAT;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getStat() == null) {
			PlayerStat stat = playerStatDbService.load(ent.getId());
			ent.setStat(stat);
		}

		if (ent.getLevelLogJson() == null) {
			ent.setLevelLogJson(JsonUtils.object2String(LevelLog.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getLevelLog() == null) {
			LevelLog ll = JsonUtils.string2Object(ent.getLevelLogJson(), LevelLog.class);
			player.setLevelLog(ll);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getLevelLog() != null) {
			ent.setLevelLogJson(JsonUtils.object2String(player.getLevelLog()));
		}
	}
}
