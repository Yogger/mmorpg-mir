package com.mmorpg.mir.model.gang.model;

import java.util.ArrayList;

public class PlayerGangVO {

	private GangInfo gangInfo;
	/** 申请 */
	private ArrayList<PlayerApplyVO> applies;
	/** 邀请 */
	private ArrayList<PlayerInviteVO> invites;
	
	private long lastQuitGangTime;

	public GangInfo getGangInfo() {
		return gangInfo;
	}

	public void setGangInfo(GangInfo gangInfo) {
		this.gangInfo = gangInfo;
	}

	public ArrayList<PlayerApplyVO> getApplies() {
		return applies;
	}

	public void setApplies(ArrayList<PlayerApplyVO> applies) {
		this.applies = applies;
	}

	public ArrayList<PlayerInviteVO> getInvites() {
		return invites;
	}

	public void setInvites(ArrayList<PlayerInviteVO> invites) {
		this.invites = invites;
	}

	public long getLastQuitGangTime() {
		return lastQuitGangTime;
	}

	public void setLastQuitGangTime(long lastQuitGangTime) {
		this.lastQuitGangTime = lastQuitGangTime;
	}

}
