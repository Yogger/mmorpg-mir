package com.mmorpg.mir.model.commonactivity.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopGoodResource;

public class CommonMarcoShopGood {
	private String id;
	private int count;

	public static CommonMarcoShopGood valueOf(String id) {
		CommonMarcoShopGood result = new CommonMarcoShopGood();
		result.id = id;
		return result;
	}

	@JsonIgnore
	public void addCount() {
		this.count++;
	}

	@JsonIgnore
	public boolean isFinishRestrict() {
		CommonMarcoShopGoodResource resource = CommonActivityConfig.getInstance().commonShopGoodStorage.get(this.id,
				true);
		if (resource.getRestrictCount() == 0) {
			return false;
		}
		return this.count >= resource.getRestrictCount();
	}

	@JsonIgnore
	public boolean isRestrictGood() {
		CommonMarcoShopGoodResource resource = CommonActivityConfig.getInstance().commonShopGoodStorage.get(this.id,
				true);
		return resource.getRestrictCount() == 0;
	}

	// getter - setter
	public String getId() {
		return id;
	}

	public int getCount() {
		return count;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
