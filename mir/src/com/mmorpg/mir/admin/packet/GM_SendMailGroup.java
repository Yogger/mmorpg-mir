package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.reward.model.Reward;

public class GM_SendMailGroup {
	private String title;
	private String sender;
	private String context;
	private Reward reward;
	private long expriedTime;
	private CoreConditionResource[] receiveCondition;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public CoreConditionResource[] getReceiveCondition() {
		return receiveCondition;
	}

	public void setReceiveCondition(CoreConditionResource[] receiveCondition) {
		this.receiveCondition = receiveCondition;
	}

	public long getExpriedTime() {
		return expriedTime;
	}

	public void setExpriedTime(long expriedTime) {
		this.expriedTime = expriedTime;
	}

}
