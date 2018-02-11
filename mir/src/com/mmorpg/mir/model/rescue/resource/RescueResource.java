package com.mmorpg.mir.model.rescue.resource;

import com.mmorpg.mir.model.rescue.model.RescueType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class RescueResource {
	@Id
	private String id;

	/** 采集物Id */
	private String gatherId;
	/** 怪物id */
	private String monsterId;
	/** 任务完成需要采集数量/杀怪数量 */
	private int complete;
	/** 任务接取需要的军衔等级 */
	private int militaryLevel;
	/** 领奖NPC，不填就自动完成 */
	private String rewardNpc;
	/** 奖励Id */
	private String chooserRewardId;
	/** 下一个任务Id */
	private String nextId;
	/** 任务所属的国家 */
	private int country;
	/** 任务类型(采集- GATHER,对话-CHAT,杀怪-MONSTER) */
	private RescueType type;
	/** 当前所处第几环(1-5) */
	private int index;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGatherId() {
		return gatherId;
	}

	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}

	public String getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	public int getMilitaryLevel() {
		return militaryLevel;
	}

	public void setMilitaryLevel(int militaryLevel) {
		this.militaryLevel = militaryLevel;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public RescueType getType() {
		return type;
	}

	public void setType(RescueType type) {
		this.type = type;
	}

	public String getChooserRewardId() {
		return chooserRewardId;
	}

	public void setChooserRewardId(String chooserRewardId) {
		this.chooserRewardId = chooserRewardId;
	}

	public String getRewardNpc() {
		return rewardNpc;
	}

	public void setRewardNpc(String rewardNpc) {
		this.rewardNpc = rewardNpc;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
