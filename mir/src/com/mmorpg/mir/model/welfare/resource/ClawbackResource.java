package com.mmorpg.mir.model.welfare.resource;

import java.util.Map;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ClawbackResource {

	@Id
	private int eventId;// 事件ID
	private int type;// z追回类型
	private String openModuleId;// 开启条件
	private int exeNum;// 需要执行的次数
	private int runs;// 每一次需要执行的环数
	private String rewardGroupId;// 奖励groupId
	private String copperActionGroupId;// 铜币消耗groupId
	private String goldActionGroupId;// 金币消耗groupId
	private Map<Integer, Integer> vipExtraCount; // VIP的额外执行次数

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getExeNum() {
		return exeNum;
	}

	public void setExeNum(int exeNum) {
		this.exeNum = exeNum;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public String getRewardGroupId() {
		return rewardGroupId;
	}

	public void setRewardGroupId(String rewardGroupId) {
		this.rewardGroupId = rewardGroupId;
	}

	public String getCopperActionGroupId() {
		return copperActionGroupId;
	}

	public void setCopperActionGroupId(String copperActionGroupId) {
		this.copperActionGroupId = copperActionGroupId;
	}

	public String getGoldActionGroupId() {
		return goldActionGroupId;
	}

	public void setGoldActionGroupId(String goldActionGroupId) {
		this.goldActionGroupId = goldActionGroupId;
	}

	public String getOpenModuleId() {
		return openModuleId;
	}

	public void setOpenModuleId(String openModuleId) {
		this.openModuleId = openModuleId;
	}

	public Map<Integer, Integer> getVipExtraCount() {
		return vipExtraCount;
	}

	public void setVipExtraCount(Map<Integer, Integer> vipExtraCount) {
		this.vipExtraCount = vipExtraCount;
	}

}