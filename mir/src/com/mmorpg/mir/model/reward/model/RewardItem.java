package com.mmorpg.mir.model.reward.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 奖励子项
 * 
 * @author Kuang Hao
 * @since v1.0 2012-12-20
 * 
 */
public class RewardItem {
	/** 奖励类型 */
	private transient RewardType rewardType;

	private String type;
	private String code;
	/** 数量 */
	private int amount;
	/** 上下文参数 */
	private Map<String, String> parms;

	public RewardItem clone() {
		RewardItem rewardItem = new RewardItem();
		rewardItem.setAmount(amount);
		rewardItem.setCode(code);
		if (rewardType == null) {
			rewardType = RewardType.typeOf(type);
		}
		rewardItem.setRewardType(rewardType);
		rewardItem.setType(type);
		if (parms != null) {
			Map<String, String> newMap = new HashMap<String, String>(parms);
			rewardItem.setParms(newMap);
		}
		return rewardItem;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof RewardItem) {
			RewardItem other = (RewardItem) obj;
			if (rewardType == other.getRewardType() || (type != null && type.equals(other.type))) {
				if (code == other.getCode() || (code != null && code.equals(other.getCode()))) {
					if (parms == other.getParms() || (parms != null && parms.equals(other.getParms())))
						return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		Map<String, String> p1 = new HashMap<String, String>();
		Map<String, String> p2 = new HashMap<String, String>();
		p1.put("a", "123");
		p2.put("a", "123");
		System.out.println(p1 == p2);
		System.out.println(p1 == p2 || (p1 != null && p1.equals(p2)));
		p1 = null;
	}

	public static RewardItem valueOf(RewardType rewardType, String code, int amount) {
		return valueOf(rewardType, code, amount, null);
	}

	public static RewardItem valueOf(RewardType rewardType, String code, int amount, Map<String, String> parms) {
		RewardItem item = new RewardItem();
		item.rewardType = rewardType;
		item.type = rewardType.name();
		item.code = code;
		item.amount = amount;
		item.parms = parms;
		return item;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public void setRewardType(RewardType rewardType) {
		this.rewardType = rewardType;
		this.type = rewardType.name();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	public void putParms(String key, String value) {
		if (parms == null) {
			parms = new HashMap<String, String>();
		}
		parms.put(key, value);
	}

	public Map<String, String> getParms() {
		return parms;
	}

	public void setParms(Map<String, String> parms) {
		this.parms = parms;
	}

}
