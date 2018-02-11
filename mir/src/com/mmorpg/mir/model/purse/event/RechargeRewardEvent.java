package com.mmorpg.mir.model.purse.event;

import com.windforce.common.event.event.IEvent;

public class RechargeRewardEvent implements IEvent {

	private long owner;

	private long amount;

	public static RechargeRewardEvent valueOf(long owner, long amount) {
		RechargeRewardEvent cre = new RechargeRewardEvent();
		cre.owner = owner;
		cre.amount = amount;
		return cre;
	}

	@Override
	public long getOwner() {
		return this.owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

}
