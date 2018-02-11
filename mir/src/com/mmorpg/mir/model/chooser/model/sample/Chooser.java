package com.mmorpg.mir.model.chooser.model.sample;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class Chooser {
	@Id
	private String id;
	/** chooser开启限制条件 */
	private String[] conditionIds;
	/** 选择器组 */
	private ItemGroup[] itemGroups;

	/**
	 * 计算结果
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<String> calcResult() {
		List<String> result = new ArrayList<String>();
		for (ItemGroup group : getItemGroups()) {
			result.addAll(group.selectResult());
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public ItemGroup[] getItemGroups() {
		return itemGroups;
	}

	public void setItemGroups(ItemGroup[] itemGroups) {
		this.itemGroups = itemGroups;
	}

}
