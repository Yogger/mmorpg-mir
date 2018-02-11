package com.mmorpg.mir.model.welfare.model;

import java.util.Date;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.DateUtils;

/***
 * 礼金汇总
 * 
 * @author 37.com
 * 
 */
public class GiftCollect {
	@Transient
	private transient Player owner;
	/** 今日获得的礼金 */
	private long amount;
	/** 累计获得礼金 */
	private long totalAmount;
	/** 最后重置时间 */
	private long lastResetTime;

	public static GiftCollect valueOf() {
		GiftCollect result = new GiftCollect();
		return result;
	}

	@JsonIgnore
	public void refreshDeprect() {
		if (!DateUtils.isToday(new Date(lastResetTime))) {
			lastResetTime = System.currentTimeMillis();
			amount = 0L;
		}
	}

	@JsonIgnore
	public void addAmount(long amounts) {
		this.refreshDeprect();
		this.amount += amounts;
		addTotalAmount(amounts);
	}
	
	@JsonIgnore
	synchronized public void addTotalAmount(long amounts){
		this.totalAmount += amounts;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

}
