package com.mmorpg.mir.model.item.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.item.model.TreasureHistory;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class TreasureHistoryEntity implements IEntity<Long> {

	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long id;

	@Lob
	private String treasureHistoryJson;

	@Transient
	private TreasureHistory treasureHistory;

	@Transient
	private transient EntityCacheService<Long, TreasureHistoryEntity> entityCachService;

	public static TreasureHistoryEntity valueOf(Long id) {
		TreasureHistoryEntity result = new TreasureHistoryEntity();
		result.id = id;
		result.treasureHistoryJson = JsonUtils.object2String(TreasureHistory.valueOf());
		return result;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTreasureHistoryJson() {
		return treasureHistoryJson;
	}

	public void setTreasureHistoryJson(String treasureHistoryJson) {
		this.treasureHistoryJson = treasureHistoryJson;
	}

	public TreasureHistory getTreasureHistory() {
		if (treasureHistory == null) {
			treasureHistory = JsonUtils.string2Object(this.treasureHistoryJson, TreasureHistory.class);
		}
		return treasureHistory;
	}

	public void setTreasureHistory(TreasureHistory treasureHistory) {
		this.treasureHistory = treasureHistory;
	}

	public EntityCacheService<Long, TreasureHistoryEntity> getEntityCachService() {
		return entityCachService;
	}

	public void setEntityCachService(EntityCacheService<Long, TreasureHistoryEntity> entityCachService) {
		this.entityCachService = entityCachService;
	}

	@Override
	public boolean serialize() {
		if (treasureHistory != null) {
			treasureHistoryJson = JsonUtils.object2String(treasureHistory);
		}
		return true;
	}

	public void update() {
		entityCachService.writeBack(this.id, this);
	}

}
