package com.mmorpg.mir.model.shop.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.reward.model.Reward;

public class BuyResult {

	private Integer code;
	private Reward reward;
	private CoreActions actions;

	public static BuyResult valueOf(Reward reward, CoreActions actions) {
		BuyResult result = new BuyResult();
		result.reward = reward;
		result.actions = actions;
		return result;
	}

	public static BuyResult valueOf(int code) {
		BuyResult result = new BuyResult();
		result.code = code;
		return result;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return code == null;
	}

	public Reward getReward() {
		return reward;
	}

	public CoreActions getActions() {
		return actions;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public void setActions(CoreActions actions) {
		this.actions = actions;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
