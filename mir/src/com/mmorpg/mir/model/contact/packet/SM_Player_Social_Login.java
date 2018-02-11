package com.mmorpg.mir.model.contact.packet;

import com.mmorpg.mir.model.contact.entity.ContactEnt;
import com.mmorpg.mir.model.contact.model.SocialNetData;

public class SM_Player_Social_Login {

	private boolean displayOffline;
	private boolean displayHead;
	private boolean publicMapInfo;
	private boolean disbandAddFriends;

	private int fansNums;

	private SocialNetData mySocialInfo;

	public static SM_Player_Social_Login valueOf(ContactEnt ent, SocialNetData data) {
		SM_Player_Social_Login login = new SM_Player_Social_Login();
		login.displayHead = ent.isShowHead();
		login.displayOffline = ent.isDisplayOnline();
		login.publicMapInfo = ent.isOpenMapInfo();
		login.mySocialInfo = data;
		login.fansNums = ent.getMyRelationData().getFans().size();
		login.disbandAddFriends = ent.isDisbandAddFriends();
		return login;
	}

	public boolean isDisplayOffline() {
		return displayOffline;
	}

	public void setDisplayOffline(boolean displayOffline) {
		this.displayOffline = displayOffline;
	}

	public boolean isDisplayHead() {
		return displayHead;
	}

	public void setDisplayHead(boolean displayHead) {
		this.displayHead = displayHead;
	}

	public boolean isPublicMapInfo() {
		return publicMapInfo;
	}

	public void setPublicMapInfo(boolean publicMapInfo) {
		this.publicMapInfo = publicMapInfo;
	}

	public SocialNetData getMySocialInfo() {
		return mySocialInfo;
	}

	public void setMySocialInfo(SocialNetData mySocialInfo) {
		this.mySocialInfo = mySocialInfo;
	}

	public int getFansNums() {
		return fansNums;
	}

	public void setFansNums(int fansNums) {
		this.fansNums = fansNums;
	}

	public boolean isDisbandAddFriends() {
    	return disbandAddFriends;
    }

	public void setDisbandAddFriends(boolean disbandAddFriends) {
    	this.disbandAddFriends = disbandAddFriends;
    }

}
