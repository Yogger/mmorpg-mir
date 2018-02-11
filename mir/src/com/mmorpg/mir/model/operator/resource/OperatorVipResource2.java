package com.mmorpg.mir.model.operator.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class OperatorVipResource2 {

	@Id
	private Integer id;
	
	private int[] vipLevel;
	
	private RewardType type;

	private String rewardId;
	
	public RewardType getType() {
		return type;
	}

	public void setType(RewardType type) {
		this.type = type;
	}

	public int[] getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int[] vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public enum RewardType{
		NICKNAME, LEVEL
	}
}
