package com.mmorpg.mir.model.operator.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class SubInformation {

	private transient Player owner;
	/** 已经领取 */
	private boolean rewarded;
	/** 已经验证 */
	private boolean confirmation;

	public static SubInformation valueOf() {
		SubInformation telphone = new SubInformation();
		return telphone;
	}

	@JsonIgnore
	public void confirmation() {
		if (!rewarded && !confirmation) {
			confirmation = true;
		}
	}

	public boolean isRewarded() {
		return rewarded;
	}

	public void setRewarded(boolean rewarded) {
		this.rewarded = rewarded;
	}

	public boolean isConfirmation() {
		return confirmation;
	}

	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

}
