package com.mmorpg.mir.model.boss.manager;

import java.util.ArrayList;
import java.util.Map;

import com.mmorpg.mir.model.boss.model.BossDropInfo;
import com.mmorpg.mir.model.boss.model.BossHistory;
import com.mmorpg.mir.model.boss.model.BossView;
import com.mmorpg.mir.model.boss.resource.BossResource;

public interface IBossManager {
	void initAll();

	void reload();

	BossHistory loadOrCreateBossEntity(BossResource bossResource);

	Map<String, BossView> getAllBossStatus(int group);

	ArrayList<BossDropInfo> getAllDropInfo();

	void updateBossHistory(BossHistory boss);

	BossResource getBossResource(String key, boolean throwException);

	Class<?> getResourceClass();
}
