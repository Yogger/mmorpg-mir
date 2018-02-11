package com.mmorpg.mir.model.gang.model;

public class PlayerInvite {
	private long gangId;
	private String gangName;
	private String invitor;
	private long invitorId;
	private long date;

	public static PlayerInvite valueOf(long invitorId, String invitor, Gang gang) {
		PlayerInvite pi = new PlayerInvite();
		pi.setInvitor(invitor);
		pi.setInvitorId(invitorId);
		pi.setDate(System.currentTimeMillis());
		pi.setGangId(gang.getId());
		pi.setGangName(gang.getName());
		return pi;
	}

	public PlayerInviteVO createVO() {
		PlayerInviteVO vo = new PlayerInviteVO();
		vo.setDate(date);
		vo.setGangId(gangId);
		vo.setGangName(gangName);
		vo.setDate(date);
		return vo;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public String getInvitor() {
		return invitor;
	}

	public void setInvitor(String invitor) {
		this.invitor = invitor;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gangId ^ (gangId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerInvite other = (PlayerInvite) obj;
		if (gangId != other.gangId)
			return false;
		return true;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public long getInvitorId() {
    	return invitorId;
    }

	public void setInvitorId(long invitorId) {
    	this.invitorId = invitorId;
    }
	
}
