package com.mmorpg.mir.model.invest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.invest.model.InvestHistory;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class InvestHistoryEntity implements IEntity<Long> {
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long id;

	@Lob
	private String investHistoryJson;

	@Transient
	private InvestHistory investHistory;

	@Transient
	private transient EntityCacheService<Long, InvestHistoryEntity> entityCachService;

	public static InvestHistoryEntity valueOf(long id) {
		InvestHistoryEntity result = new InvestHistoryEntity();
		result.id = id;
		result.investHistoryJson = JsonUtils.object2String(InvestHistory.valueOf());
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

	public InvestHistory getInvestHistory() {
		if (investHistory == null) {
			investHistory = JsonUtils.string2Object(investHistoryJson, InvestHistory.class);
		}
		return investHistory;
	}

	public void setInvestHistory(InvestHistory investHistory) {
		this.investHistory = investHistory;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityCacheService<Long, InvestHistoryEntity> getEntityCachService() {
		return entityCachService;
	}

	public void setEntityCachService(EntityCacheService<Long, InvestHistoryEntity> entityCachService) {
		this.entityCachService = entityCachService;
	}

}
