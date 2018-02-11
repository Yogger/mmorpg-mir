package com.mmorpg.mir.model.boss.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.boss.model.BossHistory;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "b1", persister = @Persister("30s"))
public class BossEntity implements IEntity<String> {

	@Id
	@Column(length = 50)
	private String id;
	@Lob
	private String bossJson;

	@Transient
	private BossHistory bossHistory;

	public String getBossJson() {
		return bossJson;
	}

	public void setBossJson(String bossJson) {
		this.bossJson = bossJson;
	}

	public BossHistory createBossHistroy() {
		if (bossHistory == null) {
			bossHistory = BossHistory.valueOf();
		}
		return bossHistory;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean serialize() {
		bossJson = JsonUtils.object2String(bossHistory);
		return true;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BossHistory getBossHistory() {
		return bossHistory;
	}

	public void setBossHistory(BossHistory bossHistory) {
		this.bossHistory = bossHistory;
	}
}
