package com.mmorpg.mir.model.agateinvest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.agateinvest.model.InvestAgateHistory;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class InvestAgateHistoryEntity implements IEntity<Long> {
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long id;

	@Lob
	private String investHistoryJson;

	@Transient
	private InvestAgateHistory investHistory;

	@Transient
	private transient EntityCacheService<Long, InvestAgateHistoryEntity> entityCachService;

	public static InvestAgateHistoryEntity valueOf(long id) {
		InvestAgateHistoryEntity result = new InvestAgateHistoryEntity();
		result.id = id;
		result.investHistoryJson = JsonUtils.object2String(InvestAgateHistory.valueOf());
		return result;
	}

	@Override
	public boolean serialize() {
		if (investHistory != null) {
			investHistoryJson = JsonUtils.object2String(investHistory);
		}
		return true;
	}

	public void update(){
		entityCachService.writeBack(this.id, this);
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public String getInvestHistoryJson() {
		return investHistoryJson;
	}

	public void setInvestHistoryJson(String investHistoryJson) {
		this.investHistoryJson = investHistoryJson;
	}

	public InvestAgateHistory getInvestHistory() {
		if (investHistory == null) {
			investHistory = JsonUtils.string2Object(investHistoryJson, InvestAgateHistory.class);
		}
		return investHistory;
	}

	public void setInvestHistory(InvestAgateHistory investHistory) {
		this.investHistory = investHistory;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityCacheService<Long, InvestAgateHistoryEntity> getEntityCachService() {
		return entityCachService;
	}

	public void setEntityCachService(EntityCacheService<Long, InvestAgateHistoryEntity> entityCachService) {
		this.entityCachService = entityCachService;
	}

}
