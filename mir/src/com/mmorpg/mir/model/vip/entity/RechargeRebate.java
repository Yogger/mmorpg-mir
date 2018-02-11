package com.mmorpg.mir.model.vip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;

@Entity
@Cached(size = "ten", persister = @Persister("identify"))
public class RechargeRebate implements IEntity<String> {

	@Id
	@Column(length = 50)
	private String id;

	private int recharge;
	private int rebate;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean serialize() {
		return true;
	}

	public int getRecharge() {
		return recharge;
	}

	public void setRecharge(int recharge) {
		this.recharge = recharge;
	}

	public int getRebate() {
		return rebate;
	}

	public void setRebate(int rebate) {
		this.rebate = rebate;
	}

	public void setId(String id) {
		this.id = id;
	}

}
