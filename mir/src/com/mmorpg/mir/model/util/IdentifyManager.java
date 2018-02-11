package com.mmorpg.mir.model.util;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ServerConfigValue;
import com.mmorpg.mir.model.util.entity.IdentifyEntity;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;

@Component
public class IdentifyManager {

	public enum IdentifyType {
		/** 玩家id */
		PLAYER(true),
		/** 道具id */
		ITEM(true),
		/** 系统邮件id */
		MAILGROUP(true),
		/** 邮件id */
		MAIL(true),
		/** 英雄id */
		HERO(true),
		/** 店铺 */
		STALL(true),
		/** 帮会 */
		GANG(true),
		/** 成就 */
		ACHIEVEMENT(true),
		/** 怪物 **/
		MONSTER(false),
		/** 掉落物品 **/
		DROPOBJECT(false),
		/** 采集物品 **/
		GATHERABLE(false),
		/** 组队 */
		GROUP(false),
		/** 国家物件 */
		COUNTRY_OBJECT(false),
		/** 国家NPC */
		COUNTRY_NPC(false),
		/** BOSS */
		BOSS(false);

		private final boolean save;

		private IdentifyType(boolean save) {
			this.save = save;
		}

		public boolean isSave() {
			return save;
		}

	}

	private static IdentifyManager self;

	public static IdentifyManager getInstance() {
		return self;
	}

	@Inject
	private EntityCacheService<String, IdentifyEntity> identifyEntityService;
	@Autowired
	private ServerConfigValue serverConfigValue;

	private static final AtomicInteger index = new AtomicInteger();

	@PostConstruct
	protected final void init() {
		self = this;
	}

	public synchronized long getNextIdentify(IdentifyType type) {
		if (!type.isSave()) {
			return index.incrementAndGet();
		}

		// TODO 这里需要根据规则计算一个id的初始值
		final long initValue = createInitValue();
		IdentifyEntity identifyEntity = identifyEntityService.loadOrCreate(type.name(),
				new EntityBuilder<String, IdentifyEntity>() {
					@Override
					public IdentifyEntity newInstance(String id) {
						return IdentifyEntity.valueOf(id, initValue);
					}
				});
		long old = identifyEntity.getValue();
		long result = identifyEntity.getNextIdentify();
		long now = identifyEntity.getValue();
		if (now != old) {
			identifyEntityService.writeBack(identifyEntity.getId(), identifyEntity);
		}
		return result;
	}

	private long createInitValue() {
		long opid = serverConfigValue.getOpid();
		long serverid = serverConfigValue.getServerid();
		long id = (opid << 50) + (serverid << 30);
		return id;
	}
}
