package com.mmorpg.mir.model.blackshop.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.blackshop.BlackShopConfig;
import com.mmorpg.mir.model.blackshop.resource.BlackShopResource;

public class BlackShopGood {
	private String id;
	private int count;

	public static BlackShopGood valueOf(String id) {
		BlackShopGood result = new BlackShopGood();
		result.id = id;
		return result;
	}

	@JsonIgnore
	public void addCount() {
		this.count++;
	}

	@JsonIgnore
	public boolean isFinishRestrict() {
		BlackShopResource resource = BlackShopConfig.getInstance().blackShopStorage.get(this.id, true);
		if (resource.getRestrictCount() == 0) {
			return false;
		}
		return this.count >= resource.getRestrictCount();
	}

	@JsonIgnore
	public boolean isRestrictGood() {
		BlackShopResource resource = BlackShopConfig.getInstance().blackShopStorage.get(this.id, true);
		return resource.getRestrictCount() == 0;
	}

	// getter-setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
