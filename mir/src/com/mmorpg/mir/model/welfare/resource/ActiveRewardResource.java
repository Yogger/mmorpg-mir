package com.mmorpg.mir.model.welfare.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ActiveRewardResource {

	@Id
	private int activeValue; // 活跃值
	private String groupId; // TODO 礼包包含的物品,后期会改为礼包

	private long giftCount;

	public int getActiveValue() {
		return activeValue;
	}

	public void setActiveValue(int activeValue) {
		this.activeValue = activeValue;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public long getGiftCount() {
		return giftCount;
	}

	public void setGiftCount(long giftCount) {
		this.giftCount = giftCount;
	}

}
