package com.mmorpg.mir.model.blackshop.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class BlackShopResource {
	public static final String GROUP_INDEX = "group_index";
	@Id
	private String id;

	@Index(name = GROUP_INDEX)
	private String groupId;
	private int weight;
	private String rewardId;
	private String[] buyActionIds;

	private String i18name;
	/** 限购次数 0为不限购商品 */
	private int restrictCount;

	@Transient
	private transient CoreActions buyActions;

	@JsonIgnore
	public CoreActions getBuyActions() {
		if (null == buyActions) {
			buyActions = CoreActionManager.getInstance().getCoreActions(1, buyActionIds);
		}
		return buyActions;
	}

	@JsonIgnore
	public boolean isRestrictGood() {
		return this.restrictCount > 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public String[] getBuyActionIds() {
		return buyActionIds;
	}

	public void setBuyActionIds(String[] buyActionIds) {
		this.buyActionIds = buyActionIds;
	}

	public int getRestrictCount() {
		return restrictCount;
	}

	public void setRestrictCount(int restrictCount) {
		this.restrictCount = restrictCount;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getI18name() {
		return i18name;
	}

	public void setI18name(String i18name) {
		this.i18name = i18name;
	}

}
