package com.mmorpg.mir.module.player.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.module.player.entity.PlayerEnt;
import com.mmorpg.mir.module.player.packet.vo.PlayerShortInfo;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.orm.Querier;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.collection.ConcurrentHashSet;

@Component
public class PlayerManager {

	/**
	 * 所有的账号与角色.<账号,<serverid,角色信息>>
	 */
	private ConcurrentHashMap<String, ConcurrentHashMap<Integer, List<PlayerShortInfo>>> accountToPlayer = new ConcurrentHashMap<>();

	/**
	 * 所有名字信息,减少数据库查询
	 */
	private ConcurrentHashSet<String> allNames = new ConcurrentHashSet<>();

	@Inject
	private EntityCacheService<Long, PlayerEnt> playerCacheService;

	@Autowired
	private Querier querier;

	@PostConstruct
	public void init() {
		List<Object[]> allPlayerShortInfo = querier.list(Object[].class, "PlayerEnt.playerShortInfo");
		for (Object[] objects : allPlayerShortInfo) {
			// playerId,account,serverId,name,role
			PlayerShortInfo shortInfo = new PlayerShortInfo();
			shortInfo.setPlayerId((long) objects[0]);
			shortInfo.setAccount((String) objects[1]);
			shortInfo.setServerId((int) objects[2]);
			shortInfo.setName((String) objects[3]);
			shortInfo.setRole((int) objects[4]);
			if (!accountToPlayer.containsKey(shortInfo.getAccount())) {
				accountToPlayer.put(shortInfo.getAccount(), new ConcurrentHashMap<Integer, List<PlayerShortInfo>>());
			}
			ConcurrentHashMap<Integer, List<PlayerShortInfo>> serverToPlayer = accountToPlayer
					.get(shortInfo.getAccount());
			if (!serverToPlayer.containsKey(shortInfo.getServerId())) {
				serverToPlayer.put(shortInfo.getServerId(), new ArrayList<>());
			}
			accountToPlayer.get(shortInfo.getAccount()).get(shortInfo.getServerId()).add(shortInfo);
			allNames.add(shortInfo.getName());
		}
	}
}
