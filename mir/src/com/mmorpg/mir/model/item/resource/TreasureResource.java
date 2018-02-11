package com.mmorpg.mir.model.item.resource;

import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.item.model.TreasureType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class TreasureResource {
	@Id
	private String id;
	/** 探宝类型 */
	private TreasureType treasureType;
	/** 档次 */
	private int grade;
	/** 探宝次数 */
	private int count;
	/** 不使用道具奖励的chooserGroup */
	private String rewardChooserGroupId;
	/** 使用道具后的奖励 */
	private String rewardItemChooserGroupId;
	/** 需要消耗的道具份数 */
	private int itemCount;
	/** 优先消耗道具 */
	private CoreActionResource[] itemPriorityActs;
	/** 消耗金钱 */
	private CoreActionResource[] goldActs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TreasureType getTreasureType() {
		return treasureType;
	}

	public void setTreasureType(TreasureType treasureType) {
		this.treasureType = treasureType;
	}

	public String getRewardChooserGroupId() {
		return rewardChooserGroupId;
	}

	public void setRewardChooserGroupId(String rewardChooserGroupId) {
		this.rewardChooserGroupId = rewardChooserGroupId;
	}

	public CoreActionResource[] getItemPriorityActs() {
		return itemPriorityActs;
	}

	public void setItemPriorityActs(CoreActionResource[] itemPriorityActs) {
		this.itemPriorityActs = itemPriorityActs;
	}

	public CoreActionResource[] getGoldActs() {
		return goldActs;
	}

	public void setGoldActs(CoreActionResource[] goldActs) {
		this.goldActs = goldActs;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getRewardItemChooserGroupId() {
		return rewardItemChooserGroupId;
	}

	public void setRewardItemChooserGroupId(String rewardItemChooserGroupId) {
		this.rewardItemChooserGroupId = rewardItemChooserGroupId;
	}

}
