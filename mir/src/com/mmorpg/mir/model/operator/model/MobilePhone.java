package com.mmorpg.mir.model.operator.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.operator.packet.SM_MobilePhone_Confirmation;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class MobilePhone {

	private transient Player owner;
	/** 已经领取 */
	private boolean rewarded;
	/** 已经验证 */
	private boolean confirmation;

	public static MobilePhone valueOf() {
		MobilePhone telphone = new MobilePhone();
		return telphone;
	}

	@JsonIgnore
	public void confirmation() {
		if (!rewarded && !confirmation) {
			confirmation = true;
			PacketSendUtility.sendPacket(owner, new SM_MobilePhone_Confirmation());
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
