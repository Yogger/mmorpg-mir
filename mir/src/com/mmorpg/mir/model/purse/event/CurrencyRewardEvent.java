package com.mmorpg.mir.model.purse.event;

import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.windforce.common.event.event.IEvent;

public class CurrencyRewardEvent implements IEvent {

	private long owner;

	private long amount;

	private CurrencyType type;

	public static CurrencyRewardEvent valueOf(long owner, long amount, CurrencyType type) {
		CurrencyRewardEvent cre = new CurrencyRewardEvent();
		cre.owner = owner;
		cre.amount = amount;
		cre.type = type;
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

	public CurrencyType getType() {
		return type;
	}

	public void setType(CurrencyType type) {
		this.type = type;
	}

}
