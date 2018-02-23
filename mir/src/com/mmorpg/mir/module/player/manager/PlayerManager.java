package com.mmorpg.mir.module.player.manager;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.module.player.entity.PlayerEnt;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityCacheService;

@Component
public class PlayerManager {

	@Inject
	private EntityCacheService<Long, PlayerEnt> playerCacheService;
}
