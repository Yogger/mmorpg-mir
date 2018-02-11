package com.mmorpg.mir.model.vip.model;

public class RechargeHistory implements Comparable<RechargeHistory> {
	private String orderId;
	private int gold;
	private long time;

	public static RechargeHistory valueOf(String orderId, int gold, long time) {
		RechargeHistory rh = new RechargeHistory();
		rh.gold = gold;
		rh.orderId = orderId;
		rh.time = time;
		return rh;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RechargeHistory other = (RechargeHistory) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int compareTo(RechargeHistory o) {
		return (int) (o.getTime() - time);
	}

}
